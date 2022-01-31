import computation.contextfreegrammar.*;
import java.util.ArrayList;
import java.util.List;

public class MyGrammar {
	public static ContextFreeGrammar makeGrammar() {
		// You can write your code here to make the context-free grammar from the assignment.

    // Create all variables in grammar
    Variable S0 = new Variable("S0");
    Variable S = new Variable("S");
    Variable T = new Variable("T");
    Variable T1 = new Variable("T1");
    Variable F = new Variable("F");
    Variable F1 = new Variable("F1");
    Variable S1 = new Variable("S1");
    Variable P = new Variable("P");
    Variable M = new Variable("M");
    Variable R = new Variable("R");
    Variable L = new Variable("L");

    // Create all terminals in grammar
    Terminal Unit = new Terminal('1');
    Terminal Zero = new Terminal('0');
    Terminal x = new Terminal('x');
    Terminal plus = new Terminal('+');
    Terminal multiply = new Terminal('*');
    Terminal left = new Terminal('(');
    Terminal right = new Terminal(')');

    // S0, S -> ST1
    Rule r1 = new Rule(S0, new Word(S, T1));
    Rule r2 = new Rule(S, new Word(S, T1));

    // S0, S, T -> TF1
    Rule r3 = new Rule(S0, new Word(T, F1));
    Rule r4 = new Rule(S, new Word(T, F1));
    Rule r5 = new Rule(T, new Word(T, F1));

    // S0, S, T, F -> S1R
    Rule r6 = new Rule(S0, new Word(S1, R));
    Rule r7 = new Rule(S, new Word(S1, R));
    Rule r8 = new Rule(T, new Word(S1, R));
    Rule r9 = new Rule(F, new Word(S1, R));

    // T1 -> PT
    Rule r10 = new Rule(T1, new Word(P, T));

    // F1 -> MF
    Rule r11 = new Rule(F1, new Word(M, F));

    // S1 -> LS
    Rule r12 = new Rule(S1, new Word(L, S));

    // S0, S, T, F -> 1
    Rule r13 = new Rule(S0, new Word(Unit));
    Rule r14 = new Rule(S, new Word(Unit));
    Rule r15 = new Rule(T, new Word(Unit));
    Rule r16 = new Rule(F, new Word(Unit));

    // S0, S, T, F -> 0
    Rule r17 = new Rule(S0, new Word(Zero));
    Rule r18 = new Rule(S, new Word(Zero));
    Rule r19 = new Rule(T, new Word(Zero));
    Rule r20 = new Rule(F, new Word(Zero));

    // S0, S, T, F -> x
    Rule r21 = new Rule(S0, new Word(x));
    Rule r22 = new Rule(S, new Word(x));
    Rule r23 = new Rule(T, new Word(x));
    Rule r24 = new Rule(F, new Word(x));

    // P -> +
    Rule r25 = new Rule(P, new Word(plus));

    // M -> *
    Rule r26 = new Rule(M, new Word(multiply));

    // R -> )
    Rule r27 = new Rule(R, new Word(right));

    // L -> (
    Rule r28 = new Rule(L, new Word(left));

    List<Rule> rules = new ArrayList<>();
    rules.add(r1);
    rules.add(r2);
    rules.add(r3);
    rules.add(r4);
    rules.add(r5);
    rules.add(r6);
    rules.add(r7);
    rules.add(r8);
    rules.add(r9);
    rules.add(r10);
    rules.add(r11);
    rules.add(r12);
    rules.add(r13);
    rules.add(r14);
    rules.add(r15);
    rules.add(r16);
    rules.add(r17);
    rules.add(r18);
    rules.add(r19);
    rules.add(r20);
    rules.add(r21);
    rules.add(r22);
    rules.add(r23);
    rules.add(r24);
    rules.add(r25);
    rules.add(r26);
    rules.add(r27);
    rules.add(r28);

		return new ContextFreeGrammar(rules);
	}
}
