/* jcifs smb client library in Java
 * Copyright (C) 2002  "Michael B. Allen" <jcifs at samba dot org>
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

import java.security.Principal;

import java.util.Set;
import java.util.TreeSet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import jcifs.rap.group.GroupUsersInfo;

import jcifs.rap.user.UserManagement;

import jcifs.smb.NtlmPasswordAuthentication;

public class AuthenticatedRequest extends HttpServletRequestWrapper {

	private final Principal principal;

    private final String authType;

    private Set groups;

	public AuthenticatedRequest(HttpServletRequest request, Principal principal,
            String authType) {
		super((request instanceof AuthenticatedRequest) ? (HttpServletRequest)
                ((AuthenticatedRequest) request).getRequest() : request);
		this.principal = principal;
        this.authType = authType;
	}

	public String getRemoteUser() {
        return principal.getName();
	}

	public Principal getUserPrincipal() {
		return principal;
	}

	public String getAuthType() {
		return authType;
	}

    public boolean isUserInRole(String role) {
        if (super.isUserInRole(role)) return true;
        if (role == null) return false;
        Set groups = getGroups();
        return (groups != null) ? groups.contains(role.toUpperCase()) : false;
    }

    private Set getGroups() {
        if (groups != null) return groups;
        try {
            Principal principal = getUserPrincipal();
            UserManagement userManagement;
            String username;
            if (principal instanceof NtlmPasswordAuthentication) {
                NtlmPasswordAuthentication auth = (NtlmPasswordAuthentication)
                        principal;
                username = auth.getUsername();
                String target = auth.getDomain();
                if ("?".equals(target)) target = null;
                if (auth.getPassword() == null) auth = null;
                userManagement = new UserManagement(target, auth);
            } else {
                username = principal.getName();
                int index = username.indexOf('@');
                if (index != -1) username = username.substring(0, index);
                userManagement = new UserManagement();
            }
            Set groups = new TreeSet();
            GroupUsersInfo[] groupList =
                    userManagement.netUserGetGroups(username, 0);
            if (groupList != null) {
                for (int i = groupList.length - 1; i >= 0; i--) {
                    groups.add(groupList[i].name.toUpperCase());
                }
            }
            return (this.groups = groups);
        } catch (Exception ex) {
            ex.printStackTrace();
            return (groups = null);
        }
    }

}

