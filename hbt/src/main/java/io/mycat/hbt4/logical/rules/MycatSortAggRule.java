/**
 * Copyright (C) <2020>  <chen junwen>
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 */
package io.mycat.hbt4.logical.rules;


import io.mycat.hbt4.MycatConvention;
import io.mycat.hbt4.MycatConverterRule;
import io.mycat.hbt4.MycatRules;
import io.mycat.hbt4.logical.rel.MycatSortAgg;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.tools.RelBuilderFactory;
import org.apache.calcite.util.ImmutableIntList;

public class MycatSortAggRule extends MycatConverterRule {
    public MycatSortAggRule(final MycatConvention out,
                            RelBuilderFactory relBuilderFactory) {
        super(Aggregate.class, project ->
                        true,
                MycatRules.convention, out, relBuilderFactory, "MycatSortAggRule");
    }

    public RelNode convert(RelNode rel) {
        final Aggregate agg = (Aggregate) rel;
        if (!Aggregate.isSimple(agg)) {
            return null;
        }
        final RelTraitSet inputTraits = rel.getCluster()
                .traitSet().replace(MycatConvention.INSTANCE).replace(out)
                .replace(
                        RelCollations.of(
                                ImmutableIntList.copyOf(
                                        agg.getGroupSet().asList())));
        final RelTraitSet selfTraits = inputTraits.replace(
                RelCollations.of(
                        ImmutableIntList.identity(agg.getGroupSet().cardinality())));
        return  MycatSortAgg.create(
                selfTraits.replace(out),
                convert(agg.getInput(), inputTraits),
                agg.getGroupSet(),
                agg.getGroupSets(),
                agg.getAggCallList());
    }
}