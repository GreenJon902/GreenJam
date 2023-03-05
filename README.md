# GreenJam
Very early WIP  
Effectively a programming language but every thing is configurable, you will be able to add completely new syntax structures and change how it compiles and into what (e.g. machine code, assembly, or perhaps a different language like c, or something
altogether different like yml to json conversion)

[![CodeFactor](https://www.codefactor.io/repository/github/greenjon902/greenjam/badge)](https://www.codefactor.io/repository/github/greenjon902/greenjam)
[![Java CI with Maven](https://github.com/GreenJon902/GreenJam/actions/workflows/maven.yml/badge.svg)](https://github.com/GreenJon902/GreenJam/actions/workflows/maven.yml)

### Syntax Modifications
One of the core parts of GreenJam is that everything is customizable, therefor syntax can be changed, appended too or removed. (default syntax and preset syntax packs will be available so you have something to build off of or to just no have to worry about it)

Take for example this script, it creates the syntax for a variable and a list completely from scratch.  
While it may look like a lot, much of it will also be used in other parts of the language.

_Note: we are using `//` for comments as there is no current support._  

[Demonstration based of the PaserTest](https://github.com/GreenJon902/GreenJam/blob/01245799fb8636733721342b2428fd67723e8d12/src/test/java/com/greenjon902/greenJam/parser/ParserTest.java#L11)
```
;; SYNTAX IGNORED ADD "\n";

;; SYNTAX RULE ADD character `a`;  // Add a few characters that to be used
;; SYNTAX RULE ADD character `f`;  // in variable names. Other characters
;; SYNTAX RULE ADD character `o`;  // not inculded as not needed in demo.

;; SYNTAX RULE ADD word REPEATING character;  // Just means n of that group 
                                              // directly after one anouther 
;; SYNTAX RULE ADD identifier `<[word]>`;  // We tell it to record it as
                                           // as a string.

;; SYNTAX RULE ADD keyword `<0public>0 `;  // Declare some keywords and record.
;; SYNTAX RULE ADD keyword `<0private>0 `;
;; SYNTAX RULE ADD keyword `<0static>0 `;
;; SYNTAX RULE ADD keywords REPEATING keyword;  // There can be multiple
                                                // on one variable.

;; SYNTAX RULE ADD number `1`;  // Again add some numbers for demo.
;; SYNTAX RULE ADD number `2`;
;; SYNTAX RULE ADD number `3`;
;; SYNTAX RULE ADD number `4`;


;; SYNTAX RULE ADD list_item `<[number]>, `;  // Tell it it needs a comma.
;; SYNTAX RULE ADD list_contents REPEATING list_item;  // Mutliple items in list.

// The last item of the list doesn't need a comma, use join to connect the nodes
// together so its not [[1, 2, 3], [4]], but [1, 2, 3, 4]. Also some stuff to
// get all nodes at the same level.
;; SYNTAX RULE ADD recorded_number `<[number]>`;
;; SYNTAX RULE ADD last_list_item `{recorded_number}`;
;; SYNTAX RULE ADD list_contents JOIN repeating_list_item last_list_item;

;; SYNTAX RULE ADD list `\{{list_contents}\}`; // Lists need to be surrounded by a { 
                                               // that has to be escaped. We also
                                               // need last term that has no comma.
                                                             
                                                             

;; SYNTAX RULE ADD variable_declaration `{1keywords}{0identifier}`;
;; SYNTAX RULE ADD variable_declaration `{identifier}`;
;; SYNTAX RULE ADD variable_assignment `{0variable_declaration} = {1list}`;

;; ROOT_NODE SET variable_assignment;  // Tell it that this is what it should parse.

public static foo = {1, 2, 3, 4} // The actual line that we now parse
```
There are many things that are going to be changed and improved, for example character ignoring doesn't work too well.  
I also intend to add a method to make it so if I use a repeating rule followed by a different rule (like for the list) i can have them all in the same node instead of having `AstNode{AstNode{1, 2, 3}, 4}` as the result.
There will also be many more features to make building syntax easier.