// Reference.java
// (C) 2009 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 03.04.2009 on http://yacy.net
//
// This is a part of YaCy, a peer-to-peer based web search engine
//
// $LastChangedDate$
// $LastChangedRevision$
// $LastChangedBy$
//
// LICENSE
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package de.anomic.kelondro.text;

import de.anomic.kelondro.index.Row.Entry;

public interface Reference {

    public String toPropertyForm();
    
    public Entry toKelondroEntry();

    public String metadataHash();

    public long lastModified();
    
    public String toString();
    
    public boolean isNewer(Reference other);
 
    public boolean isOlder(Reference other);

    public int hashCode();
    
    public void join(final Reference oe);
    
    public int positions();
    
    public int maxposition();
    
    public int minposition();
    
    public int position(int p);
    
    public void addPosition(int position);
    
    public int distance();
        
}
