/*
 * UnionGroup.java
 * 
 * Copyright (c) 2013, Instituto Superior Técnico. All rights reserved.
 * 
 * This file is part of bennu-core.
 * 
 * bennu-core is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * bennu-core is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with bennu-core. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.bennu.core.domain.groups;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;

/**
 * Union composition group.
 * 
 * @see PersistentGroup
 */
public final class PersistentUnionGroup extends PersistentUnionGroup_Base {
    protected PersistentUnionGroup(Set<PersistentGroup> children) {
        super();
        getChildrenSet().addAll(children);
    }

    @Override
    public Group toGroup() {
        return UnionGroup.of(getChildrenSet().stream().map(g -> g.toGroup()));
    }

    @Override
    protected void gc() {
        getChildrenSet().clear();
        super.gc();
    }

    /**
     * Get or create instance of a {@link PersistentUnionGroup} between the requested children.
     * 
     * @param children the groups to make a {@link PersistentUnionGroup} on.
     * @return {@link PersistentUnionGroup} instance
     * @see #getInstance(Set)
     */
    public static PersistentUnionGroup getInstance(final PersistentGroup... children) {
        return getInstance(new HashSet<>(Arrays.asList(children)));
    }

    /**
     * Get or create instance of a {@link PersistentUnionGroup} between the requested children.
     * 
     * @param children the groups to make a {@link PersistentUnionGroup} on.
     * @return {@link PersistentUnionGroup} instance
     */
    public static PersistentUnionGroup getInstance(final Set<PersistentGroup> children) {
        return singleton(() -> select(children), () -> new PersistentUnionGroup(children));
    }

    private static Optional<PersistentUnionGroup> select(final Set<PersistentGroup> children) {
        Stream<PersistentUnionGroup> intersection = null;
        for (final PersistentGroup child : children) {
            if (intersection == null) {
                intersection = child.getUnionsSet().stream().filter(g -> g.getChildrenSet().size() == children.size());
            } else {
                intersection = intersection.filter(g -> g.getChildrenSet().contains(child));
            }
        }
        return intersection != null ? intersection.findAny() : Optional.empty();
    }
}
