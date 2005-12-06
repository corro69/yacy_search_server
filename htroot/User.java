//User.java 
//-----------------------
//part of the AnomicHTTPD caching proxy
//(C) by Michael Peter Christen; mc@anomic.de
//first published on http://www.anomic.de
//Frankfurt, Germany, 2004
//
//This File is contributed by Alexander Schier
//last major change: 12.11.2005
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//Using this software in any meaning (reading, learning, copying, compiling,
//running) means that you agree that the Author(s) is (are) not responsible
//for cost, loss of data or any harm that may be caused directly or indirectly
//by usage of this softare or this documentation. The usage of this software
//is on your own risk. The installation and usage (starting/running) of this
//software may allow other people or application to access your computer and
//any attached devices and is highly dependent on the configuration of the
//software which must be done by the user of the software; the author(s) is
//(are) also not responsible for proper configuration and usage of the
//software, even if provoked by documentation provided together with
//the software.
//
//Any changes to this file according to the GPL as documented in the file
//gpl.txt aside this file in the shipment you received can be done to the
//lines that follows this copyright notice here, but changes must not be
//done inside the copyright notive above. A re-distribution must contain
//the intact and unchanged copyright notice.
//Contributions and changes to the program code must be marked as such.


//You must compile this file with
//javac -classpath .:../Classes Message.java
//if the shell's current path is HTROOT

import java.io.IOException;

import de.anomic.data.userDB;
import de.anomic.http.httpHeader;
import de.anomic.plasma.plasmaSwitchboard;
import de.anomic.server.serverCodings;
import de.anomic.server.serverObjects;
import de.anomic.server.serverSwitch;

public class User{
    
    
    public static serverObjects respond(httpHeader header, serverObjects post, serverSwitch env) {
        serverObjects prop = new serverObjects();
        plasmaSwitchboard sb = plasmaSwitchboard.getSwitchboard();
        userDB.Entry entry=null;

        //default values
        prop.put("logged_in", 0);
        prop.put("logged-in_limit", 0);
        prop.put("status", 0);
        entry=sb.userDB.proxyAuth(((String) header.get(httpHeader.AUTHORIZATION, "xxxxxx")));
        if(entry != null){
        		prop.put("logged-in_identified-by", 1);
        }else{
        		entry=sb.userDB.ipAuth(((String)header.get("CLIENTIP", "xxxxxx")));
        		if(entry != null){
        			prop.put("logged-in_identified-by", 0);
        		}
        }
        if(entry != null){
        		prop.put("logged-in", 1);
        		prop.put("logged-in_username", entry.getUserName());
        		if(entry.getTimeLimit().intValue() > 0){
        			prop.put("logged-in_limit", 1);
        			prop.put("logged-in_limit_timelimit", entry.getTimeLimit());
        			prop.put("logged-in_limit_timeused", entry.getTimeUsed());
        		}
        }
        if(post!= null && entry != null){
        		if(post.containsKey("logout")){
        			entry.logout(((String)header.get("CLIENTIP", "xxxxxx")));
        			prop.put("logged-in", 0);
        		}else if(post.containsKey("changepass")){
        			prop.put("status", 1); //password
        			if(entry.getMD5EncodedUserPwd().equals(serverCodings.encodeMD5Hex(entry.getUserName()+":"+post.get("oldpass", "")))){
        			if(post.get("newpass").equals(post.get("newpass2"))){
        			if(!post.get("newpass", "").equals("")){
        				try {
							entry.setProperty(userDB.Entry.MD5ENCODED_USERPWD_STRING, serverCodings.encodeMD5Hex(entry.getUserName()+":"+post.get("newpass", "")));
							prop.put("status_password", 0); //changes
						} catch (IOException e) {}
        			}else{
        				prop.put("status_password", 3); //empty
        			}
        			}else{
        				prop.put("status_password", 2); //pws do not match
        			}
        			}else{
        				prop.put("status_password", 1); //old pw wrong
        			}
        		}
        }
        // return rewrite properties
        return prop;
    }
}
