/* jcifs smb client library in Java
 * Copyright (C) 2004  "Michael B. Allen" <jcifs at samba dot org>
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

package jcifs.spnego;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.net.UnknownHostException;

import java.security.AccessControlContext;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import jcifs.Config;
import jcifs.UniAddress;

import jcifs.netbios.NbtAddress;

import jcifs.ntlmssp.NtlmFlags;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbSession;

public class Authentication {

    private static final byte[] NTLMSSP_SIGNATURE = new byte[] {
        (byte) 'N', (byte) 'T', (byte) 'L', (byte) 'M',
        (byte) 'S', (byte) 'S', (byte) 'P', (byte) 0
    };

    private static final boolean KERBEROS_SUPPORTED = getKerberosSupport();

    private Properties properties = new Properties();

    private boolean client;

    private byte[] nextToken;

    private Object subject;

    private Principal principal;

    private Object context;

    public Authentication() {
        this(null);
    }

    public Authentication(Properties properties) {
        setProperties(properties);
    }

    public byte[] getNextToken() {
        return nextToken;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = (properties != null) ? properties : new Properties();
    }

    public String getProperty(String property) {
        String value = getProperties().getProperty(property);
        if (value == null) value = Config.getProperty(property);
        return (value != null) ? value : System.getProperty(property);
    }

    public void setProperty(String property, String value) {
        if (value != null) {
            getProperties().setProperty(property, value);
        } else {
            getProperties().remove(property);
        }
    }

    public void init(String[] mechanisms) throws AuthenticationException {
        reset();
        client = true;
        if (mechanisms == null || mechanisms.length == 0) {
            String unicode = getProperty("jcifs.smb.client.useUnicode");
            boolean useUnicode = (unicode != null) ?
                    Boolean.valueOf(unicode).booleanValue() : true;
            int flags = NtlmFlags.NTLMSSP_NEGOTIATE_NTLM | (useUnicode ?
                    NtlmFlags.NTLMSSP_NEGOTIATE_UNICODE :
                            NtlmFlags.NTLMSSP_NEGOTIATE_OEM);
            String domain = getProperty("jcifs.smb.client.domain");
            String workstation = null;
            try {
                workstation = NbtAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) { }
            nextToken = new Type1Message(flags, domain,
                    workstation).toByteArray();
            return;
        } else {
            int contextFlags = 0;
            byte[] mechanismToken = null;
            String initialMechanism = mechanisms[0];
            if (SpnegoConstants.NTLMSSP_MECHANISM.equals(initialMechanism)) {
                String unicode = getProperty("jcifs.smb.client.useUnicode");
                boolean useUnicode = (unicode != null) ?
                        Boolean.valueOf(unicode).booleanValue() : true;
                int flags = NtlmFlags.NTLMSSP_NEGOTIATE_NTLM | (useUnicode ?
                        NtlmFlags.NTLMSSP_NEGOTIATE_UNICODE :
                                NtlmFlags.NTLMSSP_NEGOTIATE_OEM);
                String domain = getProperty("jcifs.smb.client.domain");
                String workstation = null;
                try {
                    workstation = NbtAddress.getLocalHost().getHostName();
                } catch (UnknownHostException ex) { }
                mechanismToken = new Type1Message(flags, domain,
                        workstation).toByteArray();
            } else if (SpnegoConstants.KERBEROS_MECHANISM.equals(
                    initialMechanism) ||
                            SpnegoConstants.LEGACY_KERBEROS_MECHANISM.equals(
                                    initialMechanism)) {
                ClientAction action = new ClientAction(null);
                String useSubjectCredsOnly = getProperty(
                        "javax.security.auth.useSubjectCredsOnly");
                boolean subjectOnly = (useSubjectCredsOnly == null) ? true :
                        Boolean.valueOf(useSubjectCredsOnly).booleanValue();
                if (subjectOnly) {
                    try {
                        String name = getProperty(
                                "javax.security.auth.login.name");
                        String password = getProperty(
                                "javax.security.auth.login.password");
                        Object loginContext = newLoginContext.newInstance(
                                new Object[] { "jcifs.spnego.initiate",
                                        createLoginHandler(name, password) });
                        login.invoke(loginContext, null);
                        subject = getSubject.invoke(loginContext, null);
                    } catch (Exception ex) {
                        throw new AuthenticationException("Unable to login: " +
                                ex, ex);
                    }
                }
                try {
                    mechanismToken = (byte[]) ((subject != null) ?
                            doAsPrivileged.invoke(null, new Object[] {
                                    subject, action, null }) : action.run());
                } catch (Exception ex) {
                    throw new AuthenticationException(
                            "Error processing token: " + ex, ex);
                }
                try {
                    if (((Boolean) getCredDelegState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.DELEGATION;
                    }
                    if (((Boolean) getMutualAuthState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.MUTUAL_AUTHENTICATION;
                    }
                    if (((Boolean) getReplayDetState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.REPLAY_DETECTION;
                    }
                    if (((Boolean) getSequenceDetState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.SEQUENCE_CHECKING;
                    }
                    if (((Boolean) getAnonymityState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.ANONYMITY;
                    }
                    if (((Boolean) getConfState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.CONFIDENTIALITY;
                    }
                    if (((Boolean) getIntegState.invoke(context,
                            null)).booleanValue()) {
                        contextFlags |= NegTokenInit.INTEGRITY;
                    }
                } catch (Exception ex) {
                    throw new AuthenticationException(ex.getMessage());
                }
            } else {
                throw new UnsupportedMechanismException(
                        "Unsupported initial mechanism: " + initialMechanism);
            }
            nextToken = new NegTokenInit(mechanisms, contextFlags,
                    mechanismToken, null).toByteArray();
        }
    }

    public void reset() {
        client = false;
        nextToken = null;
        subject = null;
        principal = null;
        context = null;
    }

    public void process(byte[] token) throws AuthenticationException {
        if (token == null) throw new NullPointerException("Null token.");
        if (getPrincipal() != null) {
            throw new IllegalStateException("Context already completed.");
        }
        if (isNtlm(token)) {
            processNtlm(token);
        } else {
            processSpnego(token);
        }
    }

    private UniAddress getDomainController() throws UnknownHostException {
        boolean loadBalance = false;
        String domainController =
                getProperty("jcifs.http.domainController");
        if (domainController == null) {
            domainController = getProperty("jcifs.smb.client.domain");
            String balance = getProperty("jcifs.http.loadBalance");
            loadBalance = (balance != null) ?
                    Boolean.valueOf(balance).booleanValue() : true;

        }
        return loadBalance ? new UniAddress(
                NbtAddress.getByName(domainController, 0x1c, null)) :
                        UniAddress.getByName(domainController, true);
    }

    private void processNtlm(byte[] token) throws AuthenticationException {
        byte[] challenge = null;
        try {
            switch (token[8]) {
            case 1:
                if (client) {
                    throw new AuthenticationException(
                            "NTLM Type 1 message received by client.");
                }
                challenge = SmbSession.getChallenge(getDomainController());
                nextToken = new Type2Message(new Type1Message(token),
                        challenge, null).toByteArray();
                break;
            case 2:
                if (!client) {
                    throw new AuthenticationException(
                            "NTLM Type 2 message received by server.");
                }
                String workstation = null;
                try {
                    workstation = NbtAddress.getLocalHost().getHostName();
                } catch (UnknownHostException ex) { }
                nextToken = new Type3Message(new Type2Message(token),
                        getProperty("jcifs.smb.client.password"),
                        getProperty("jcifs.smb.client.domain"),
                        getProperty("jcifs.smb.client.username"),
                        workstation).toByteArray();
                break;
            case 3:
                if (client) {
                    throw new AuthenticationException(
                            "NTLM Type 3 message received by client.");
                }
                Type3Message type3 = new Type3Message(token);
                byte[] lmResponse = type3.getLMResponse();
                if (lmResponse == null) lmResponse = new byte[0];
                byte[] ntResponse = type3.getNTResponse();
                if (ntResponse == null) ntResponse = new byte[0];
                nextToken = null;
                challenge = SmbSession.getChallenge(getDomainController());
                NtlmPasswordAuthentication auth =
                        new NtlmPasswordAuthentication(type3.getDomain(),
                                type3.getUser(), challenge, lmResponse,
                                        ntResponse);
                SmbSession.logon(getDomainController(), auth);
                principal = auth;
                break;
            default:
                throw new AuthenticationException(
                        "Unrecognized NTLM Token Type: " + token[8]);
            }
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationException(
                    "Error performing NTLM authentication: " + ex, ex);
        }
    }

    private void processSpnego(byte[] token) throws AuthenticationException {
        try {
            SpnegoToken spnegoToken;
            String mechanism;
            byte[] mechanismToken;
            switch (token[0]) {
            case (byte) 0x60:
                NegTokenInit initToken = new NegTokenInit(token);
                spnegoToken = initToken;
                mechanismToken = initToken.getMechanismToken();
                mechanism = (mechanismToken != null) ?
                        initToken.getMechanisms()[0] : null;
                break;
            case (byte) 0xa1:
                NegTokenTarg targToken = new NegTokenTarg(token);
                spnegoToken = targToken;
                mechanismToken = targToken.getMechanismToken();
                mechanism = targToken.getMechanism();
                break;
            default:
                throw new AuthenticationException("Unrecognized SPNEGO Token.");
            }
            if (isNtlm(mechanismToken)) {
                processNtlm(mechanismToken);
                if (nextToken == null) return;
                int result = (getPrincipal() == null) ?
                        NegTokenTarg.ACCEPT_INCOMPLETE :
                                NegTokenTarg.ACCEPT_COMPLETED;
                nextToken = new NegTokenTarg(result,
                        SpnegoConstants.NTLMSSP_MECHANISM, nextToken,
                                null).toByteArray();
            } else if (mechanism != null && KERBEROS_SUPPORTED) {
                processKerberos(mechanism, mechanismToken);
            } else {
                if (!(spnegoToken instanceof NegTokenInit)) {
                    nextToken = new NegTokenTarg(NegTokenTarg.REJECTED, null,
                            null, null).toByteArray();
                    return;
                }
                String[] mechanisms =
                        ((NegTokenInit) spnegoToken).getMechanisms();
                if (mechanisms == null) {
                    nextToken = new NegTokenTarg(NegTokenTarg.REJECTED, null,
                            null, null).toByteArray();
                    return;
                }
                List mechList = Arrays.asList(mechanisms);
                if (KERBEROS_SUPPORTED) {
                    if (mechList.contains(SpnegoConstants.KERBEROS_MECHANISM)) {
                        mechanism = SpnegoConstants.KERBEROS_MECHANISM;
                    } else if (mechList.contains(
                            SpnegoConstants.LEGACY_KERBEROS_MECHANISM)) {
                        mechanism = SpnegoConstants.LEGACY_KERBEROS_MECHANISM;
                    } else {
                        mechanism = SpnegoConstants.NTLMSSP_MECHANISM;
                    }
                } else {
                    mechanism = SpnegoConstants.NTLMSSP_MECHANISM;
                }
                if (mechList.contains(mechanism)) {
                    nextToken = new NegTokenTarg(NegTokenTarg.ACCEPT_INCOMPLETE,
                            mechanism, null, null).toByteArray();
                } else {
                    nextToken = new NegTokenTarg(NegTokenTarg.REJECTED, null,
                            null, null).toByteArray();
                }
            }
        } catch (AuthenticationException ex) {
ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
ex.printStackTrace();
            throw new AuthenticationException(
                    "Error performing SPNEGO negotiation: " + ex, ex);
        }
    }

    private void processKerberos(String mechanism, byte[] token)
            throws AuthenticationException {
        try {
            if (client) {
                ClientAction action = new ClientAction(token);
                token = (byte[]) ((subject != null) ?
                        doAsPrivileged.invoke(null, new Object[] {
                                subject, action, null }) : action.run());
                int result;
                if (((Boolean) isEstablished.invoke(context,
                        null)).booleanValue()) {
                    result = NegTokenTarg.ACCEPT_COMPLETED;
                    principal = (Principal) newKerberosPrincipal.newInstance(
                            new Object[] { getSrcName.invoke(context,
                                    null).toString() });
                } else {
                    result = NegTokenTarg.ACCEPT_INCOMPLETE;
                }
                nextToken = new NegTokenTarg(result, mechanism, token,
                        null).toByteArray();
            } else {
                ServerAction action = new ServerAction(token);
                if (subject == null) {
                    String useSubjectCredsOnly = getProperty(
                            "javax.security.auth.useSubjectCredsOnly");
                    boolean subjectOnly = (useSubjectCredsOnly == null) ? true :
                            Boolean.valueOf(useSubjectCredsOnly).booleanValue();
                    if (subjectOnly) {
                        String name = getProperty(
                                "jcifs.spnego.servicePrincipal");
                        String password =
                                getProperty("jcifs.spnego.servicePassword");
                        Object loginContext = newLoginContext.newInstance(
                                new Object[] { "jcifs.spnego.accept",
                                        createLoginHandler(name, password) });
                        login.invoke(loginContext, null);
                        subject = getSubject.invoke(loginContext, null);
                    }
                }
                token = (byte[]) ((subject != null) ?
                        doAsPrivileged.invoke(null, new Object[] {
                                subject, action, null }) : action.run());
                int result;
                if (((Boolean) isEstablished.invoke(context,
                        null)).booleanValue()) {
                    result = NegTokenTarg.ACCEPT_COMPLETED;
                    principal = (Principal) newKerberosPrincipal.newInstance(
                            new Object[] { getSrcName.invoke(context,
                                    null).toString() });
                } else {
                    result = NegTokenTarg.ACCEPT_INCOMPLETE;
                }
                nextToken = new NegTokenTarg(result, mechanism, token,
                        null).toByteArray();
            }
        } catch (Exception ex) {
            throw new AuthenticationException(
                    "Error performing Kerberos authentication: " + ex, ex);
        }
    }

    private static boolean isNtlm(byte[] token) {
        if (token == null || token.length < 8) return false;
        for (int i = 0; i < 8; i++) {
            if (NTLMSSP_SIGNATURE[i] != token[i]) return false;
        }
        return true;
    }

    private class ClientAction implements PrivilegedExceptionAction {

        private byte[] token;

        public ClientAction(byte[] token) {
            this.token = (token != null) ? token : new byte[0];
        }

        public Object run() throws Exception {
            if (Authentication.this.context == null) {
                Object gss = getGssInstance.invoke(null, null);
                Object servicePrincipal = createName.invoke(gss, new Object[] {
                        getProperty("jcifs.spnego.servicePrincipal"), null });
                Object credential = null;
                String user = getProperty("javax.security.auth.login.name");
                if (user != null) {
                    Object userName = createName.invoke(gss,
                            new Object[] { user, null });
                    // 0 = GSSContext.DEFAULT_LIFETIME
                    // 1 = GSSCredential.INITIATE_ONLY
                    credential = createCredential.invoke(gss, new Object[] {
                            userName, new Integer(0), kerberosOid,
                                    new Integer(1) });
                }
                // 0 = GSSContext.DEFAULT_LIFETIME
                Authentication.this.context = createInitiateContext.invoke(gss,
                        new Object[] { servicePrincipal, kerberosOid,
                                credential, new Integer(0) });
            }
            return initSecContext.invoke(Authentication.this.context,
                    new Object[] { token, new Integer(0),
                            new Integer(token.length) });
        }

    }

    private class ServerAction implements PrivilegedExceptionAction {

        private byte[] token;

        public ServerAction(byte[] token) {
            this.token = (token != null) ? token : new byte[0];
        }

        public Object run() throws Exception {
            if (Authentication.this.context == null) {
                Object gss = getGssInstance.invoke(null, null);
                Object servicePrincipal = createName.invoke(gss, new Object[] {
                        getProperty("jcifs.spnego.servicePrincipal"), null });
                // 0 = GSSContext.DEFAULT_LIFETIME
                // 2 = GSSCredential.ACCEPT_ONLY
                Object credential = createCredential.invoke(gss, new Object[] {
                        servicePrincipal, new Integer(0), kerberosOid,
                                new Integer(2) });
                Authentication.this.context = createAcceptContext.invoke(gss,
                        new Object[] { credential });
            }
            return acceptSecContext.invoke(Authentication.this.context,
                    new Object[] { token, new Integer(0),
                            new Integer(token.length) });
        }

    }

    private static Object createLoginHandler(String name, String password) {
        return Proxy.newProxyInstance(callbackHandler.getClassLoader(),
                new Class[] { callbackHandler },
                        new SpnegoLoginHandler(name, password));
    }

    private static boolean getKerberosSupport() {
        try {
            Class gss = Class.forName("org.ietf.jgss.GSSManager");
            getGssInstance = gss.getMethod("getInstance", null);
            Object manager = getGssInstance.invoke(null, null);
            Object[] mechs = (Object[])
                    gss.getMethod("getMechs", null).invoke(manager, null);
            if (mechs == null) return false;
            for (int i = mechs.length - 1; i >= 0; i--) {
                if (SpnegoConstants.KERBEROS_MECHANISM.equals(
                        mechs[i].toString())) {
                    kerberosOid = mechs[i];
                    Class oidClass = Class.forName("org.ietf.jgss.Oid");
                    createName = gss.getMethod("createName", new Class[] {
                            String.class, oidClass });
                    Class nameClass = Class.forName("org.ietf.jgss.GSSName");
                    createCredential = gss.getMethod("createCredential",
                            new Class[] { nameClass, Integer.TYPE, oidClass,
                                    Integer.TYPE });
                    Class credentialClass =
                            Class.forName("org.ietf.jgss.GSSCredential");
                    createInitiateContext = gss.getMethod("createContext",
                            new Class[] { nameClass, oidClass, credentialClass,
                                    Integer.TYPE });
                    createAcceptContext = gss.getMethod("createContext",
                            new Class[] { credentialClass });
                    Class contextClass =
                            Class.forName("org.ietf.jgss.GSSContext");
                    initSecContext = contextClass.getMethod("initSecContext",
                            new Class[] { byte[].class, Integer.TYPE,
                                    Integer.TYPE });
                    acceptSecContext =
                            contextClass.getMethod("acceptSecContext",
                                    new Class[] { byte[].class, Integer.TYPE,
                                            Integer.TYPE });
                    getSrcName = contextClass.getMethod("getSrcName", null);
                    getCredDelegState =
                            contextClass.getMethod("getCredDelegState", null);
                    getMutualAuthState =
                            contextClass.getMethod("getMutualAuthState", null);
                    getReplayDetState =
                            contextClass.getMethod("getReplayDetState", null);
                    getSequenceDetState =
                            contextClass.getMethod("getSequenceDetState", null);
                    getAnonymityState =
                            contextClass.getMethod("getAnonymityState", null);
                    getConfState = contextClass.getMethod("getConfState", null);
                    getIntegState =
                            contextClass.getMethod("getIntegState", null);
                    isEstablished =
                            contextClass.getMethod("isEstablished", null);
                    Class loginContext = Class.forName(
                            "javax.security.auth.login.LoginContext");
                    callbackHandler = Class.forName(
                            "javax.security.auth.callback.CallbackHandler");
                    newLoginContext = loginContext.getConstructor(new Class[] {
                            String.class, callbackHandler });
                    Class kerberosPrincipal = Class.forName(
                            "javax.security.auth.kerberos.KerberosPrincipal");
                    newKerberosPrincipal = kerberosPrincipal.getConstructor(
                            new Class[] { String.class });
                    login = loginContext.getMethod("login", null);
                    getSubject = loginContext.getMethod("getSubject", null);
                    Class subjectClass =
                            Class.forName("javax.security.auth.Subject");
                    doAsPrivileged = subjectClass.getMethod("doAsPrivileged",
                            new Class[] { subjectClass,
                                    PrivilegedExceptionAction.class,
                                            AccessControlContext.class });
                    return true;
                }
            }
        } catch (Throwable t) { }
        return false;
    }

    private static class SpnegoLoginHandler implements InvocationHandler {

        private final String username;

        private final String password;

        public SpnegoLoginHandler(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            if (!"handle".equals(method.getName())) {
                throw new UnsupportedOperationException(method.getName());
            }
            Object[] callbacks = (Object[]) args[0];
            Class nameCallback =
                    Class.forName("javax.security.auth.callback.NameCallback");
            Method setName = nameCallback.getMethod("setName",
                    new Class[] { String.class });
            Class passwordCallback = Class.forName(
                    "javax.security.auth.callback.PasswordCallback");
            Method setPassword = passwordCallback.getMethod("setPassword",
                    new Class[] { char[].class });
            for (int i = 0; i < callbacks.length; i++) {
                Object callback = callbacks[i];
                if (nameCallback.isInstance(callback)) {
                    setName.invoke(callback, new Object[] { username });
                }
                if (passwordCallback.isInstance(callback)) {
                    setPassword.invoke(callback,
                            new Object[] { (password != null) ?
                                    password.toCharArray() : null });
                }
            }
            return null;
        }

    }

    // handles for dynamic Kerberos calls

    private static Object kerberosOid;

    private static Method getGssInstance;

    private static Method createName;

    private static Method createCredential;

    private static Method createInitiateContext;

    private static Method createAcceptContext;

    private static Method initSecContext;

    private static Method acceptSecContext;

    private static Method getSrcName;

    private static Method getCredDelegState;

    private static Method getMutualAuthState;

    private static Method getReplayDetState;

    private static Method getSequenceDetState;

    private static Method getAnonymityState;

    private static Method getConfState;

    private static Method getIntegState;

    private static Method isEstablished;

    private static Class callbackHandler;

    private static Constructor newKerberosPrincipal;

    private static Constructor newLoginContext;

    private static Method login;

    private static Method getSubject;

    private static Method doAsPrivileged;

}
