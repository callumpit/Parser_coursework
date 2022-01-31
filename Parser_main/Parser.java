import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;
import java.util.ArrayList;
import java.util.List;
import computation.contextfreegrammar.Rule;

public class Parser implements IParser {

  private List<Derivation> derivationTracker(ContextFreeGrammar cfg, Word w) {
    Variable startVariable = cfg.getStartVariable();
    List<Derivation> active_derivations = new ArrayList<>();
    Derivation startDerivation = new Derivation(new Word(startVariable));

    // add start variable to derivation list
    active_derivations.add(startDerivation);

    int steps = 0; // step counter
    int index = 0; // index counter
    int lenWord = w.length(); // target word length
    List<Rule> grammar_rules = cfg.getRules(); // grammar rules

    while (steps < (2 * lenWord) - 1) {

      // create new list for next generation of derivations
      List<Derivation> new_active_derivations = new ArrayList<>();

      // evaluate each of the current generation of derivations
      for (Derivation eachDerivation : active_derivations) {

        // instantiate current word being evaluated
        Word currentWord = eachDerivation.getLatestWord();

        // iterate through each possible rule and check each symbol for application
        // (left to right)
        for (Rule rule : grammar_rules) {
          index = 0;
          Word expansion = rule.getExpansion();

          // for each symbol in the latest word of each derivation.
          for (Symbol symbol : currentWord) {

            // if the symbol matches the LHS variable of the current rule
            if (symbol == rule.getVariable()) {

              // create new copy of the derivation sequence
              Derivation eachDerivationCopy = new Derivation(eachDerivation);

              // extend the derivation with the rule applied
              Word newWord = currentWord.replace(index, expansion);
              eachDerivationCopy.addStep(newWord, rule, index);

              // add extended derivation to new active derivations
              new_active_derivations.add(eachDerivationCopy);

              // ensure only one rule is applied per step
              break;

            } else {
              // if rule doesn't apply to current symbol move to next one.
              index += 1;
            }
          }
          
        }
      }
      // set active_derivations to new_active_derivations.
      active_derivations = new_active_derivations;
      steps += 1;
    }
    return active_derivations;
  }


  public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {

    // Deal with empty word case. Since cfg is in CNF, the only occurance of ε in
    // derivation is for ""
    // ----------------------------
    // for the input word "":
    // if there exists a rule in the grammar for the start symbol:
    // S0 -> ε
    // then the empty word is in the language, return true,
    // if the rule does not exist then the empty word is not in the language,
    // return false.
    // ----------------------------
    if ((w.toString().equals("ε"))
        && (cfg.getRules().contains(new Rule(cfg.getStartVariable(), new Word(""))))) {
      // System.out.println(String.format("TRUE, %s IS IN THE LANGUAGE", w.toString()));
      return true;
    } else if ((w.toString().equals("ε"))
        && (!cfg.getRules().contains(new Rule(cfg.getStartVariable(), new Word(""))))) {
      // System.out.println(String.format("False, %s IS NOT IN THE LANGUAGE", w.toString()));
      return false;
    }
    // now for all other cases.

    // create list of derivations
    List<Derivation> possible_derivations = derivationTracker(cfg, w);

    // create list of possible words from possible derivations
    List<Word> possibleWords = new ArrayList<>();
    for (Derivation finalDerivation : possible_derivations) {
      possibleWords.add(finalDerivation.getLatestWord());
    }

    // check if target word is contained in list of possible words
    if (possibleWords.contains(w)) {
      // System.out.println(String.format("TRUE, %s IS IN THE LANGUAGE", w.toString()));
      return true;
    } else {
      // System.out.println(String.format("False, %s IS NOT IN THE LANGUAGE", w.toString()));
      return false;
    }
  }

  public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
    // Again deal with empty word input similarly to isInLanguage. Since grammar
    // is in CNF, only occurance of V--ε tree branch is for empty word input
    // corresponding to the tree S0
    //                           |
    //                          ε
    // This time return this empty parse tree if the rule exists in the grammar
    // and return null if not (both for "" input).
    if ((w.toString().equals("ε"))
        && (cfg.getRules().contains(new Rule(cfg.getStartVariable(), new Word(""))))) {
      return ParseTreeNode.emptyParseTree(cfg.getStartVariable());

    } else if ((w.toString().equals("ε"))
        && (!cfg.getRules().contains(new Rule(cfg.getStartVariable(), new Word(""))))) {
      // System.out.println(String.format("%s IS NOT IN THE LANGUAGE", w.toString()));
      return null;
    }
    // Now for all other cases.

    // create list of derivations
    List<Derivation> possible_derivations = derivationTracker(cfg, w);

    // create list of possible words from possible derivations
    List<Word> possibleWords = new ArrayList<>();
    for (Derivation finalDerivation : possible_derivations) {
      possibleWords.add(finalDerivation.getLatestWord());
    }

    // check if target word is contained in list of possible words, if it isn't then
    // return null
    if (!possibleWords.contains(w)) {
      // System.out.println(String.format("%s IS NOT IN THE LANGUAGE", w.toString()));
      return null;
    }
    // if target word is contained then select corresponding derivation.
    else {
      Derivation useDerivation = null;
      for (Derivation derivations : possible_derivations) {
        if (w.equals(derivations.getLatestWord())) {
          useDerivation = derivations;
          break;
        }
      }
      // iterate backwards through the corresponding derivation to build up parse tree
      int wordLength = w.length();
      int count = 0;

      // create list for tree roots (i.e. current constructions starting at the
      // terminals).
      List<ParseTreeNode> treeRoots = new ArrayList<>();

      // for each step in the derivation (going backwards)
      for (Step step : useDerivation) {

        // terminate process when first step is reached
        if (count == (2 * wordLength) - 1) {
          break;
        }

        // get the rule from step.
        Rule rule = step.getRule();

        // get the start variable and expansion from the rule
        Variable startVariable = rule.getVariable();
        Word expansion = rule.getExpansion();

        // if the expansion is a terminal
        if (expansion.isTerminal()) {

          // create a terminal tree and add it to the terminal tree list.
          Terminal terminal = new Terminal(expansion.toString().charAt(0));
          ParseTreeNode terminalNode = new ParseTreeNode(terminal);
          treeRoots.add(new ParseTreeNode(startVariable, terminalNode));

        } else {
          // otherwise the node is a variable node.
          // create a new treeRoots list
          List<ParseTreeNode> newTreeRoots = new ArrayList<>();

          // Search the treeRoots list for each of the corresponding variables.
          Symbol variable1 = expansion.get(0);
          Symbol variable2 = expansion.get(1);
          ParseTreeNode variableTree1 = null;
          ParseTreeNode variableTree2 = null;

          // check each tree root for either variable, and define a variable tree. Any
          // unused root is added to newTreeRoot list.

          boolean gotV1 = false; // booleans for checking if variables
          boolean gotV2 = false; // have been found yet.

          for (ParseTreeNode root : treeRoots) {
            // if root variable is same as variable 1 and variable 1 hasn't been found yet.
            if ((variable1.equals(root.getSymbol())) && (!gotV1)) {
              variableTree1 = root;
              gotV1 = true;

              // if root variable is same as variable 2 and variable 2 hasn't been found yet.
            } else if ((variable2.equals(root.getSymbol())) && (!gotV2)) {
              variableTree2 = root;
              gotV2 = true;

              // otherwise root is unused, so add to new root list.
            } else {
              newTreeRoots.add(root);
            }
          }

          // now create a new root from two roots corresponding to the expansion variables
          // and add to root list
          newTreeRoots.add(new ParseTreeNode(startVariable, variableTree1, variableTree2));

          // reassign treeRoots to newTreeRoots
          treeRoots = newTreeRoots;

        }
        count++;

      }
      // should now be a single tree in the treeRoot list which is returned.
      return treeRoots.get(0);
    }
  }
}
// chcp 65001