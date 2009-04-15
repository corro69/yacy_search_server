// indexContainerOrder.java
// (C) 2007 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 2007 on http://yacy.net
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

import de.anomic.kelondro.order.AbstractOrder;
import de.anomic.kelondro.order.Order;

public class ReferenceContainerOrder<ReferenceType extends Reference> extends AbstractOrder<ReferenceContainer<ReferenceType>> implements Order<ReferenceContainer<ReferenceType>>, Cloneable {

    private final ReferenceFactory<ReferenceType> factory;
    private final Order<byte[]> embeddedOrder;

    public ReferenceContainerOrder(ReferenceFactory<ReferenceType> factory, final Order<byte[]> embedOrder) {
        this.embeddedOrder = embedOrder;
        this.factory = factory;
    }

    public boolean wellformed(final ReferenceContainer<ReferenceType> a) {
        return embeddedOrder.wellformed(a.getTermHash().getBytes());
    }
    
    public void direction(final boolean ascending) {
        this.embeddedOrder.direction(ascending);
    }

    public long partition(final byte[] key, final int forks) {
        return this.embeddedOrder.partition(key, forks);
    }

    public int compare(final ReferenceContainer<ReferenceType> a, final ReferenceContainer<ReferenceType> b) {
        return this.embeddedOrder.compare(a.getTermHash().getBytes(), b.getTermHash().getBytes());
    }
    
    public boolean equal(ReferenceContainer<ReferenceType> a, ReferenceContainer<ReferenceType> b) {
        return this.embeddedOrder.equal(a.getTermHash().getBytes(), b.getTermHash().getBytes());
    }
    
    public void rotate(final ReferenceContainer<ReferenceType> zero) {
        this.embeddedOrder.rotate(zero.getTermHash().getBytes());
        this.zero = new ReferenceContainer<ReferenceType>(this.factory, new String(this.embeddedOrder.zero()), zero);
    }

    public Order<ReferenceContainer<ReferenceType>> clone() {
        return new ReferenceContainerOrder<ReferenceType>(this.factory, this.embeddedOrder.clone());
    }

    public String signature() {
        return this.embeddedOrder.signature();
    }

    public long cardinal(final byte[] key) {
        return this.embeddedOrder.cardinal(key);
    }
    
    public boolean equals(final Order<ReferenceContainer<ReferenceType>> otherOrder) {
        if (!(otherOrder instanceof ReferenceContainerOrder)) return false;
        return this.embeddedOrder.equals(((ReferenceContainerOrder<ReferenceType>) otherOrder).embeddedOrder);
    }

	public long cardinal(final ReferenceContainer<ReferenceType> key) {
		return this.embeddedOrder.cardinal(key.getTermHash().getBytes());
	}

}
