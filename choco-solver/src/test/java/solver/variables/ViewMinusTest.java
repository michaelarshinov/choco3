/**
 *  Copyright (c) 1999-2011, Ecole des Mines de Nantes
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the Ecole des Mines de Nantes nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.variables;

import choco.checker.DomainBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;
import solver.Cause;
import solver.Solver;
import solver.constraints.IntConstraintFactory;
import solver.exception.ContradictionException;
import solver.search.strategy.IntStrategyFactory;
import util.iterators.DisposableRangeIterator;
import util.iterators.DisposableValueIterator;

import java.util.Random;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 23/08/11
 */
public class ViewMinusTest {


    @Test(groups = "1s")
    public void test1() {
        Solver solver = new Solver();

        IntVar X = VariableFactory.enumerated("X", 1, 10, solver);
        IntVar Y = VariableFactory.minus(X);

        try {
//            solver.propagate();
            Assert.assertFalse(Y.isInstantiated());
            Assert.assertEquals(Y.getLB(), -10);
            Assert.assertEquals(Y.getUB(), -1);
            Assert.assertTrue(Y.contains(-5));
            Assert.assertEquals(Y.nextValue(-11), -10);
            Assert.assertEquals(Y.nextValue(-5), -4);
            Assert.assertEquals(Y.nextValue(-1), Integer.MAX_VALUE);
            Assert.assertEquals(Y.previousValue(0), -1);
            Assert.assertEquals(Y.previousValue(-4), -5);
            Assert.assertEquals(Y.previousValue(-10), Integer.MIN_VALUE);

            Y.updateLowerBound(-9, Cause.Null);
            Assert.assertEquals(Y.getLB(), -9);
            Assert.assertEquals(X.getUB(), 9);

            Y.updateUpperBound(-2, Cause.Null);
            Assert.assertEquals(Y.getUB(), -2);
            Assert.assertEquals(X.getLB(), 2);

            Y.removeValue(-4, Cause.Null);
            Assert.assertFalse(Y.contains(-4));
            Assert.assertFalse(X.contains(4));

            Y.removeInterval(-8, -6, Cause.Null);
            Assert.assertFalse(Y.contains(-8));
            Assert.assertFalse(Y.contains(-7));
            Assert.assertFalse(Y.contains(-6));
            Assert.assertFalse(X.contains(6));
            Assert.assertFalse(X.contains(7));
            Assert.assertFalse(X.contains(8));

            Assert.assertEquals(X.getDomainSize(), 4);
            Assert.assertEquals(Y.getDomainSize(), 4);

            Y.instantiateTo(-5, Cause.Null);
            Assert.assertTrue(X.isInstantiated());
            Assert.assertTrue(Y.isInstantiated());
            Assert.assertEquals(X.getValue(), 5);
            Assert.assertEquals(Y.getValue(), -5);

        } catch (ContradictionException ex) {

        }
    }

    @Test(groups = "10s")
    public void test2() {
        Random random = new Random();
        for (int seed = 0; seed < 2000; seed++) {
            random.setSeed(seed);
            Solver ref = new Solver();
            {
                IntVar[] xs = new IntVar[2];
                xs[0] = VariableFactory.bounded("x", 1, 15, ref);
                xs[1] = VariableFactory.bounded("y", -15, -1, ref);
                ref.post(IntConstraintFactory.sum(xs, VariableFactory.fixed(0, ref)));
                ref.set(IntStrategyFactory.random(xs, seed));
            }
            Solver solver = new Solver();
            {
                IntVar[] xs = new IntVar[2];
                xs[0] = VariableFactory.bounded("x", 1, 15, solver);
                xs[1] = VariableFactory.minus(xs[0]);
                solver.post(IntConstraintFactory.sum(xs, VariableFactory.fixed(0, solver)));
                solver.set(IntStrategyFactory.random(xs, seed));
            }
            ref.findAllSolutions();
            solver.findAllSolutions();
            Assert.assertEquals(solver.getMeasures().getSolutionCount(), ref.getMeasures().getSolutionCount());

        }
    }

    @Test(groups = "10s")
    public void test3() {
        Random random = new Random();
        for (int seed = 0; seed < 2000; seed++) {
            random.setSeed(seed);
            Solver ref = new Solver();
            {
                IntVar[] xs = new IntVar[2];
                xs[0] = VariableFactory.enumerated("x", 1, 15, ref);
                xs[1] = VariableFactory.enumerated("y", -15, -1, ref);
                ref.post(IntConstraintFactory.sum(xs, VariableFactory.fixed(0, ref)));
                ref.set(IntStrategyFactory.random(xs, seed));
            }
            Solver solver = new Solver();
            {
                IntVar[] xs = new IntVar[2];
                xs[0] = VariableFactory.enumerated("x", 1, 15, solver);
                xs[1] = VariableFactory.minus(xs[0]);
                solver.post(IntConstraintFactory.sum(xs, VariableFactory.fixed(0, solver)));
                solver.set(IntStrategyFactory.random(xs, seed));
            }
            ref.findAllSolutions();
            solver.findAllSolutions();
            Assert.assertEquals(solver.getMeasures().getSolutionCount(), ref.getMeasures().getSolutionCount());

        }
    }

    @Test(groups = "10s")
    public void testIt1() {
        Random random = new Random();
        for (int seed = 0; seed < 200; seed++) {
            random.setSeed(seed);
            Solver solver = new Solver();
            int[][] domains = DomainBuilder.buildFullDomains(1, -5, 5, random, random.nextDouble(), random.nextBoolean());
            IntVar o = VariableFactory.bounded("o", domains[0][0], domains[0][domains[0].length - 1], solver);
            IntVar v = VariableFactory.minus(o);
            DisposableValueIterator vit = v.getValueIterator(true);
            while (vit.hasNext()) {
                Assert.assertTrue(o.contains(-vit.next()));
            }
            vit.dispose();
            vit = v.getValueIterator(false);
            while (vit.hasPrevious()) {
                Assert.assertTrue(o.contains(-vit.previous()));
            }
            vit.dispose();
            DisposableRangeIterator rit = v.getRangeIterator(true);
            while (rit.hasNext()) {
                Assert.assertTrue(o.contains(-rit.min()));
                Assert.assertTrue(o.contains(-rit.max()));
                rit.next();
            }
            rit = v.getRangeIterator(false);
            while (rit.hasPrevious()) {
                Assert.assertTrue(o.contains(-rit.min()));
                Assert.assertTrue(o.contains(-rit.max()));
                rit.previous();
            }
        }
    }

    @Test(groups = "10s")
    public void testIt2() {
        Random random = new Random();
        for (int seed = 0; seed < 200; seed++) {
            random.setSeed(seed);
            Solver solver = new Solver();
            int[][] domains = DomainBuilder.buildFullDomains(1, -5, 5, random, random.nextDouble(), random.nextBoolean());
            IntVar o = VariableFactory.enumerated("o", domains[0], solver);
            IntVar v = VariableFactory.minus(o);
            DisposableValueIterator vit = v.getValueIterator(true);
            while (vit.hasNext()) {
                Assert.assertTrue(o.contains(-vit.next()));
            }
            vit.dispose();
            vit = v.getValueIterator(false);
            while (vit.hasPrevious()) {
                Assert.assertTrue(o.contains(-vit.previous()));
            }
            vit.dispose();
            DisposableRangeIterator rit = v.getRangeIterator(true);
            while (rit.hasNext()) {
                Assert.assertTrue(o.contains(-rit.min()));
                Assert.assertTrue(o.contains(-rit.max()));
                rit.next();
            }
            rit = v.getRangeIterator(false);
            while (rit.hasPrevious()) {
                Assert.assertTrue(o.contains(-rit.min()));
                Assert.assertTrue(o.contains(-rit.max()));
                rit.previous();
            }
        }
    }

}
