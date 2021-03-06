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
package parser.flatzinc;

import antlr.RecognitionException;
import parser.flatzinc.para.ParaserMaster;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The main entry point
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 16/07/13
 */
public class ChocoFZN {

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, RecognitionException {
        int nbCores = 1;
		boolean all = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-a")) {
				all = true;
			}
		}
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                // -p option defines the number of slaves
                nbCores = Integer.parseInt(args[i + 1]);
                // each slave has one thread
                args[i + 1] = "1";
                break;
            }
        }
		if(all){
			nbCores = 1;
		}
        if (nbCores == 1) {
            new ParseAndSolve().doMain(args);
        } else {
            // will manage one ParseAndSolve per thread
            new ParaserMaster(nbCores, args);
        }
    }
}
