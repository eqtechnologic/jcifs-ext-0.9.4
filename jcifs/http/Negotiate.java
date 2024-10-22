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

package jcifs.http;

import java.io.IOException;

import java.security.Principal;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jcifs.spnego.Authentication;
import jcifs.spnego.AuthenticationException;

import jcifs.util.Base64;

public class Negotiate {

    public static Principal authenticate(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        int index = auth.indexOf(' ');
        String mechanism = auth.substring(0, index);
        byte[] token = Base64.decode(auth.substring(index).trim());  
        Authentication authentication = new Authentication();
        try {
            authentication.process(token);
        } catch (AuthenticationException ex) {
            Throwable cause = ex.getCause();
            if (cause == null) throw new ServletException(ex.getMessage());
            if (cause instanceof IOException) throw (IOException) cause;
            throw new ServletException(ex.getMessage(), ex.getCause());
        }
        byte[] nextToken = authentication.getNextToken();
        if (nextToken != null) {
            auth = Base64.encode(nextToken);
            response.setHeader("WWW-Authenticate", mechanism + " " + auth);
        }
        Principal principal = authentication.getPrincipal();
        if (principal != null) return principal;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentLength(0);
        response.flushBuffer();
        return null;
    }

}
