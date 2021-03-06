/*
 * Copyright (c) 1999-2012, Ecole des Mines de Nantes
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Ecole des Mines de Nantes nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package solver.search.strategy.selectors.variables;

import solver.search.strategy.selectors.VariableSelector;
import solver.variables.RealVar;

/**
 * A cyclic variable selector : since a dichotomy algorithm is used, cyclic assigning is needed for instantiate
 * a real interval variable.
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 18/07/12
 */
public class Cyclic implements VariableSelector<RealVar> {

    protected int current;
    protected RealVar[] vars;

    public Cyclic(RealVar[] vars) {
        this.current = -1;
        this.vars = vars;
    }

    @Override
    public RealVar[] getScope() {
        return vars;
    }

    @Override
    public boolean hasNext() {
        int nbvars = vars.length;
        int start = current == -1 ? nbvars - 1 : current;
        int n = (current + 1) % nbvars;
        while (n != start && vars[n].isInstantiated()) {
            n = (n + 1) % nbvars;
        }
        current = n;
        return !vars[current].isInstantiated();
    }

    @Override
    public void advance() {
        // void
    }

    @Override
    public RealVar getVariable() {
        return vars[current];
    }
}
