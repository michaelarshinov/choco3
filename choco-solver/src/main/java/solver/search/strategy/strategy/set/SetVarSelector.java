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

package solver.search.strategy.strategy.set;

import solver.search.strategy.selectors.VariableSelector;
import solver.variables.SetVar;

/**
 * Heuristic to select a SetVar to branch on
 * @author Jean-Guillaume Fages
 * @since 6/10/13
 */
public final class SetVarSelector {

    /**
     * Selects the variables minimising envelopeSize-kernelSize (quite similar
     * to minDomain, or first-fail)
     */
    public static class MinDelta implements VariableSelector<SetVar> {

        SetVar[] variables;
        int small_idx;

        public MinDelta(SetVar[] variables) {
            this.variables = variables;
            this.small_idx = 0;
        }

        @Override
        public SetVar[] getScope() {
            return variables;
        }

        @Override
        public boolean hasNext() {
            int idx = 0;
            for (; idx < variables.length && variables[idx].isInstantiated(); idx++) {
            }
            return idx < variables.length;
        }

        @Override
        public void advance() {
            small_idx = 0;
            int delta = Integer.MAX_VALUE;
            for (int idx = 0; idx < variables.length; idx++) {
                SetVar variable = variables[idx];
                int d = variable.getEnvelopeSize() - variable.getKernelSize();
                if (d > 0 && d < delta) {
                    delta = d;
                    small_idx = idx;
                }
            }
        }

        @Override
        public SetVar getVariable() {
            return variables[small_idx];
        }
    }

    /**
     * Selects the variables maximising envelopeSize-kernelSize
     */
    public static class MaxDelta implements VariableSelector<SetVar> {

        SetVar[] variables;
        int small_idx;

        public MaxDelta(SetVar[] variables) {
            this.variables = variables;
            this.small_idx = 0;
        }

        @Override
        public SetVar[] getScope() {
            return variables;
        }

        @Override
        public boolean hasNext() {
            int idx = 0;
            for (; idx < variables.length && variables[idx].isInstantiated(); idx++) {
            }
            return idx < variables.length;
        }

        @Override
        public void advance() {
            small_idx = 0;
            int delta = 0;
            for (int idx = 0; idx < variables.length; idx++) {
                SetVar variable = variables[idx];
                int d = variable.getEnvelopeSize() - variable.getKernelSize();
                if (d > delta) {
                    delta = d;
                    small_idx = idx;
                }
            }
        }

        @Override
        public SetVar getVariable() {
            return variables[small_idx];
        }
    }
}
