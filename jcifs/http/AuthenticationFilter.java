/* jcifs smb client library in Java
 * Copyright (C) 2004  "Michael B. Allen" <jcifs at samba dot org>
 *                   "Jason Pugsley" <jcifs at samba dot org>
 *                   "skeetz" <jcifs at samba dot org>
 *                   "Eric Glass" <jcifs at samba dot org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package jcifs.http;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.security.Principal;

import java.util.Enumeration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jcifs.Config;
import jcifs.UniAddress;

import jcifs.netbios.NbtAddress;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.NtStatus;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;

import jcifs.util.Base64;

public class AuthenticationFilter implements Filter {

    public static final String HTTP_NEGOTIATE = "Negotiate";

    public static final String HTTP_NTLM = "NTLM";

    public static final String HTTP_BASIC = "Basic";

    private FilterConfig filterConfig;

    private String defaultDomain;

    private String domainController;

    private boolean loadBalance;

    private boolean enableNegotiate;

    private boolean enableBasic;

    private boolean insecureBasic;

    private String realm;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        Config.setProperty( "jcifs.smb.client.soTimeout", "300000" );
        Config.setProperty( "jcifs.netbios.cachePolicy", "600" );
        Enumeration parameters = filterConfig.getInitParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            if (parameter.startsWith("jcifs.")) {
                Config.setProperty(parameter,
                        filterConfig.getInitParameter(parameter));
            }
        }
        if (Boolean.valueOf(filterConfig.getInitParameter(
                "sun.security.krb5.debug")).booleanValue()) {
            System.setProperty("sun.security.krb5.debug", "true");
        }
        String loginConfig = filterConfig.getInitParameter(
                "java.security.auth.login.config");
        if (System.getProperty("java.security.auth.login.config") == null ||
                loginConfig != null) {
            try {
                URL url = filterConfig.getServletContext().getResource(
                        (loginConfig == null) ? "/WEB-INF/login.conf" :
                                loginConfig);
                if (url != null) loginConfig = url.toExternalForm();
            } catch (MalformedURLException ex) { }
            if (loginConfig != null) {
                System.setProperty("java.security.auth.login.config",
                        loginConfig);
            } else {
                URL url = getClass().getResource("/jcifs/http/login.conf");
                if (url != null) {
                    System.setProperty("java.security.auth.login.config",
                            url.toExternalForm());
                }
            }
        }
        String useSubjectCredsOnly = filterConfig.getInitParameter(
                "javax.security.auth.useSubjectCredsOnly");
        if (useSubjectCredsOnly != null) {
            System.setProperty("javax.security.auth.useSubjectCredsOnly",
                    useSubjectCredsOnly);
        }
        String krbConf =
                filterConfig.getInitParameter("java.security.krb5.conf");
        if (krbConf != null) {
            try {
                String realPath =
                        filterConfig.getServletContext().getRealPath(krbConf);
                if (realPath != null && (new File(realPath)).isFile()) {
                    krbConf = realPath;
                }
            } catch (Exception ex) { }
            System.setProperty("java.security.krb5.conf", krbConf);
        }
        String krbRealm =
                filterConfig.getInitParameter("java.security.krb5.realm");
        if (krbRealm != null) {
            System.setProperty("java.security.krb5.realm", krbRealm);
        }
        String krbKdc = filterConfig.getInitParameter("java.security.krb5.kdc");
        if (krbKdc != null) {
            System.setProperty("java.security.krb5.kdc", krbKdc);
        }
        defaultDomain = Config.getProperty("jcifs.smb.client.domain");
        domainController = Config.getProperty("jcifs.http.domainController");
        if (domainController == null) {
            domainController = defaultDomain;
            loadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
        }
        enableNegotiate =
                Config.getBoolean("jcifs.http.enableNegotiate", false);
        enableBasic = Config.getBoolean("jcifs.http.enableBasic", false);
        insecureBasic = Config.getBoolean("jcifs.http.insecureBasic", false);
        realm = Config.getProperty("jcifs.http.basicRealm");
        if (realm == null) realm = "jCIFS";
    }

    public void destroy() {
        filterConfig = null;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        try {
            init(filterConfig);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Principal principal = null;
        String authType = null;
        String msg = req.getHeader("Authorization");
        boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());
        if (msg != null && (msg.regionMatches(true, 0, "Negotiate ", 0, 10) ||
                msg.regionMatches(true, 0, "NTLM ", 0, 5) || (offerBasic &&
                        msg.regionMatches(true, 0, "Basic ", 0, 6)))) {
            authType = msg.regionMatches(true, 0, "Negotiate ", 0, 10) ?
                    HTTP_NEGOTIATE : msg.regionMatches(true, 0, "NTLM ", 0, 5) ?
                            HTTP_NTLM : HTTP_BASIC;
            try {
                if (HTTP_NEGOTIATE.equals(authType) ||
                        HTTP_NTLM.equals(authType)) {
                    principal = Negotiate.authenticate(req, resp);
                    if (principal == null) return;
                    req.getSession().setAttribute("jcifs.http.principal",
                            principal);
                    chain.doFilter(new AuthenticatedRequest(req, principal,
                            authType), resp);
                    return;
                }
                UniAddress dc = loadBalance ? new UniAddress(
                        NbtAddress.getByName(domainController, 0x1C, null)) :
                                UniAddress.getByName(domainController, true);
                String auth = new String(Base64.decode(msg.substring(6)),
                        "US-ASCII");
                int index = auth.indexOf(':');
                String user = (index != -1) ? auth.substring(0, index) : auth;
                String password = (index != -1) ? auth.substring(index + 1) :
                        "";
                index = user.indexOf('\\');
                if (index == -1) index = user.indexOf('/');
                String domain = (index != -1) ? user.substring(0, index) :
                        defaultDomain;
                user = (index != -1) ? user.substring(index + 1) : user;
                principal = new NtlmPasswordAuthentication(domain, user,
                        password);
                SmbSession.logon(dc, (NtlmPasswordAuthentication) principal);
            } catch (SmbAuthException sae) {
                fail((sae.getNtStatus() == NtStatus.NT_STATUS_ACCESS_VIOLATION),
                        req, resp);
                return;
            }
            HttpSession ssn = req.getSession();
            ssn.setAttribute("jcifs.http.principal", principal);
        } else {
            HttpSession ssn = req.getSession(false);
            if (ssn == null || (principal = (Principal)
                    ssn.getAttribute("jcifs.http.principal")) == null) {
                fail(false, req, resp);
                return;
            }
        }
        chain.doFilter(new AuthenticatedRequest(req, principal, authType),
                resp);
    }

    private void fail(boolean clearSession, HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        if (clearSession) {
            HttpSession ssn = req.getSession(false);
            if (ssn != null) ssn.removeAttribute("jcifs.http.principal");
        }
        if (enableNegotiate) resp.addHeader("WWW-Authenticate", "Negotiate");
        resp.addHeader("WWW-Authenticate", "NTLM");
        if (enableBasic && (insecureBasic || req.isSecure())) {
            resp.addHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
        }
        resp.setHeader("Connection", "close");
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.flushBuffer();
    }

}
