/*
 * LDAP Chai API
 * Copyright (c) 2006-2009 Novell, Inc.
 * Copyright (c) 2009 Jason D. Rivard
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

import com.novell.ldapchai.ChaiUser;
import com.novell.ldapchai.exception.ChaiOperationException;
import com.novell.ldapchai.exception.ChaiUnavailableException;
import com.novell.ldapchai.provider.ChaiConfiguration;
import com.novell.ldapchai.provider.ChaiProvider;
import com.novell.ldapchai.provider.ChaiProviderFactory;
import com.novell.ldapchai.provider.ChaiSetting;

/**
 * LDAP Chai API
 * Example Code
 * ----
 *
 * Make an advanced connection using the ChaiProviderFactory.  Using this
 * approach to make the connection allows more control over the options
 * used in the ChaiProvider.  The result is the same as the SimpleConnection
 * approach, a ChaiProvider instance.  The ChaiProvider instance is used
 * in turn to create a ChaiUser.
 */
public class AdvancedConnection {

    public static void main(final String[] args) {
        // connection parameters
        String ldapURL =      "ldap://ldaphost:389";
        String ldapBindDN =   "cn=admin,ou=ou,o=o";
        String ldapBindPW =   "password";


        // allocate a new ChaiConfiguration
        ChaiConfiguration chaiConfig = ChaiConfiguration.builder(ldapURL, ldapBindDN, ldapBindPW)

                // set the chai challenge/response engine to use the 'title' attribute to store its data.
                .setSetting(ChaiSetting.CR_CHAI_STORAGE_ATTRIBUTE,"title")

                // disable ldap server fail-over
                .setSetting(ChaiSetting.WATCHDOG_ENABLE,"false")

                // set the chai config to blind trust any SSL certificate
                .setSetting(ChaiSetting.PROMISCUOUS_SSL,"true")

                // enable NMAS security mode.
                .setSetting(ChaiSetting.EDIRECTORY_ENABLE_NMAS,"true")

                // build the configuration
                .build();


        try {
            // create a ChaiProviderFactory;
            ChaiProviderFactory chaiProviderFactory = ChaiProviderFactory.newProviderFactory();

            // create a ChaiProvider
            ChaiProvider provider = chaiProviderFactory.newProvider(chaiConfig);

            // create a ChaiProvider
            ChaiUser bindUser = provider.getEntryFactory().newChaiUser(ldapBindDN);

            // read the user's last name.
            String surname = bindUser.readStringAttribute(ChaiUser.ATTR_SURNAME);

            // read the bind user's surname
            System.out.println("surname = " + surname);
        } catch (ChaiUnavailableException e) {
            System.out.println("LDAP unreachable: " + e.getMessage());
        } catch (ChaiOperationException e) {
            System.out.println("LDAP error: " + e.getMessage());
        }

    }
}
