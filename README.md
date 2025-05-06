# Assignment - Text Generator

## Objectives

For this assignment, you will:

* Gain practice using Java Generics
* Gain practice with `HashMap`s (Java's own efficient implementation of dictionaries/symbol tables)
* Gain proficiency in using objects to build more complex data structures

## Description

In this assignment, we will use a basic technique in Artificial Intelligence to automatically generate text. You will write a program that reads in a piece of text and then uses that text as a basis to generate new text. The method for generating new text uses simple probability. (This is a simple approximation of how large language models like ChatGPT work to generate text as well.)

First, we read in a piece of text word by word (we consider punctuation symbols to be words) keeping track of how often each three-word sequence (trigram) appears. For example, consider the following excerpt from Rudyard Kipling’s poem "If":

    If you can keep your head when all about you
    Are losing theirs and blaming it on you,
    If you can trust yourself when all men doubt you,
    But make allowance for their doubting too;
    If you can wait and not be tired by waiting

In this excerpt, the trigrams are: "if you can", "you can keep", "can keep your", "keep your head", "your head when", "head when all", etc.

Once we have counted all of the trigrams, we can compute the probability that a word *w*<sub>3</sub> will immediately follow two other words (*w*<sub>1</sub> and *w*<sub>2</sub>) using the following equation:

*p*(*w*<sub>3</sub>|*w*<sub>1</sub>, *w*<sub>2</sub>) = *n*<sub>123</sub>/*n*<sub>12</sub>

where *n*<sub>123</sub> is the number of times we observed the sequence (*w*<sub>1</sub>*w*<sub>2</sub>*w*<sub>3</sub>) and *n*<sub>12</sub> is the number of times we observed the sequence  *w*<sub>1</sub>*w*<sub>2</sub>.

For example, let’s compute the probability that the word "can" will immediately follow the words "if you". In the excerpt above, the sequence "if you can" occurs three times. The sequence "if you" also occurs three times. So the conditional probability is given by,

*p*(*can*|*if you*) = *n*<sub>if you can</sub>/*n*<sub>if you</sub> = *3/3 = 1*

The probability that any other word comes immediately after "if you" is *0* (probabilities need to add up to 1). Consider another example. In the excerpt above, the words "you can" appear *3* times. The first time followed by "keep", the second time by "trust", and the last time by "wait". Thus,

* *p*(*keep*|*you can*) = *1/3*
* *p*(*trust*|*you can*) = *1/3*
* *p*(*wait*|*you can*) = *1/3*

Again, the probability that any other word besides "keep", "trust", or "wait" appears after "you can" is zero.

Once we have the text processed, and stored in a data structure that allows us to compute probabilities, we
can generate new text. We first pick two words to start with (for example, the first two words in the input
text). Given these two words, and the probabilities computed above, we use a random number generator to
choose the next word. We will continue this process until the new text is 400 words long (including punctuation).
When printing the new text, please generate a new line after every 20 words so that you don’t just generate
one very long, unreadable, line.

**Note**: There may be two words that were seen in the text but were never followed by any word. For example,
in the excerpt of the Kipling poem above, the words "by waiting" occur once but are never followed by a
word (since they are the last two words in the excerpt). In this case, you can either stop generating words
or feel free to think up a more creative solution.


## Classes

### `FreqList`

`FreqList` should contain a `HashMap` object (make sure to carefully read the [documentation of the class](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)) which is a dictionary. Its keys will be words and its values the number of times that each associated word occurs. When a word is added, if it already occurs in the dictionary, then its value (i.e. its frequency)
is incremented by 1. If it doesn’t exist, add the word to the `HashMap` object with a value (i.e. frequency) of 1.

Your `FreqList` class should have an instance variable that keeps track of the
number of word references added.
This is equivalent to the sum of all the values (i.e. frequencies) in the `HashMap`.

You will probably also find it helpful to override the `toString` method that
dumps out a list of the words and their reference counts.

The `FreqList` class should also have a method with the following definition:

    public String get(double p)

This method takes a `double p` as an input and returns a word from the `HashMap`. The input `p` must be between 0.0 and 1.0, otherwise the method should throw an `IllegalArgumentException`. How can we use `p`
to generate a word? In our example above, for the words "you can", the three words that can follow this pair and their associated frequency would look like: `{<"keep",1>, <"trust", 1>, <"wait, 1>}`. The sum of all of the frequencies is 3. Thus, we will return "keep" whenever
0 ≤ *p* &lt; 1/3, "trust" whenever 1/3 ≤ *p* &lt; 2/3 and "wait" whenever 2/3 ≤ p ≤ 1. If the frequency list is
empty, this method should return an empty string.

Write this class and test it thoroughly by adding a `main` method or a JUnit, instantiating a `FreqList` object and adding some Strings to make sure all methods work correctly.
We suggest implementing the `toString` method and print out a representation of the frequency list first to make sure that the table is correct
before attempting to write or test the probabilistic `get` method.

### `StringPair`

We suggest that you write the `StringPair` class next.
`StringPair` should represent a pair of two strings.
Fill in the constructor and override
the `toString` method that will return something like `<string_one,string_two>` (note that there is no space between the two words when you implement the `toString`.)
Because we will be using `StringPair` objects in `HashMap`s you will also have to implement
`equals` and `hashCode` methods (look for shortcut 1 in lecture slides) that do their comparisons and computations
based on *both* of the string values.

To make sure that you have correctly implemented these methods, you should
create a `main()` method that tests them by:

- creating multiple `StringPair` objects, for the same and different pairs of strings.
- ensuring your `equals` method correctly identifies matching and non-matching pairs. Don't forget that String equality is tested with `equals` not `==`.
- creating a <StringPair,String> `HashMap`.
- populating it with mappings for many different `StringPair`s.
- confirming that you can find and update those mappings.

### `TextGenerator`

The `TextGenerator` class should also contain a `HashMap` object. In this case, the keys will be sequences of two words (e.g., "you can") that you will represent through the `StringPair` class you created. The values will be objects
of type `FreqList`.

We have provided you with startup code in the `main` method of class `TextGenerator` that will pop up a
dialog box to allow the user to choose a file containing the input text. We have also provided the headers of
two methods for that class.

The first method, `enter(String first, String second, String third)`, will be used to build the table that will be used later to generate random text. The elements of the table are of the form

    [<Word1, Word2> -> <w1, n1>, <w2, n2>, ..., <wk, nk>]

where this notation represents an association, where the key is the word pair of type `StringPair` <Word1, Word2> and the value is the **frequency list** *<w1,n1>,<w2,n2>,...,<wk,nk>* (of type `FreqList`).

Suppose the TextGenerator object you are building is named `table` and the input file starts with "This is the time for". Then you will call `table.enter("This", "is", "the")`, which should update the entry for the word pair of
"This" and "is" to record that "the" is a possible word to follow the pair. More carefully, if there is no entry
for that word pair, then it will create one for that pair with an empty frequency list, and then add "the"
occurring once to that frequency list. If the word pair is already there, update the frequency list to record
the added occurrence of "the". Having done this, continue with `table.enter("is","the","time")`, then
`table.enter("the","time","for")`, and continuing in the same way if there is more text.

We have included some text files (ending with suffix "txt") in the `text` folder that you can use to test
your program. We’ve tried to pick files with sufficient repetition of triples.

After the input has been processed to build the table, you should generate new text. You may start with
a fixed pair of words or choose two words randomly. The method `getNextWord(String first, String second)` uses the table
generated from the input to return a randomly chosen word from the frequency list associated with the word
pair of `first` and `second`. (Of course it should choose the word using the probabilities as discussed earlier.)

Generate and print a string of at least 400 words so that we can see how your program works. When
printing the text, please generate a new line after every 20 words so that you don’t just generate one very
long, unreadable, line.

Finally, we’d like you to print the table of frequencies in some reasonable fashion so we can check to see if
your table is correct on our test input. Here are some lines of output based on the lyrics for Bob Dylan’s "[Blowin’
in the wind](https://www.bobdylan.com/songs/blowin-wind/)":


    {...
    <how,many>=Frequency List: <seas=1> <times=3> <ears=1> <roads=1> <years=2> <deaths=1>, <many,roads>=Frequency List: <must=1>, <must,the>=Frequency List: <cannon=1>, <?,the>=Frequency List: <answer=3>,...}

And

    Generated data:
    how many years can a man ? how many years can a mountain exist before they ' re forever banned ?
    the answer is blowin ' in the wind the answer is blowin ' in the wind the answer my friend
    is blowin ' in the wind . yes , how many years can a man turn his head pretending he
    just doesn ' t see ? the answer my friend is blowin ' in the wind the answer my friend
    is blowin ' in the wind the answer my friend is blowin ' in the wind the answer is blowin
    ' in the wind the answer my friend is blowin ' in the wind the answer is blowin ' in
    the sand ? yes , how many times must a man walk down before you call him a man look
    up before he can see the sky ? yes , how many times must the cannon balls fly before they
    ' re forever banned ? the answer is blowin ' in the sand ? yes , how many times must
    the cannon balls fly before they ' re forever banned ? the answer my friend is blowin ' in



This output indicates that after the word pair "how many", the words "roads", "seas", "times", "years", "ears", and "deaths" each occurred along with their frequencies. Whereas after "many roads", only "must" occurred and it only occurred once.

**Warning**: As a general rule it is good to be as specific as possible with import statements. Thus, when you import the package with the class, use:

~~~java
    import java.util.Random;
    import java.util.HashMap;
    import java.util.Map;
~~~

### `WordStream`

This class is already implemented for you. It processes a text file and breaks up the input into separate words that can be requested using the method `nextToken`. In the `main` method of the `TextGenerator` class, there is an object of type `WordStream` called `ws`. You can get successive words from `ws` by calling
`ws.nextToken()` repeatedly. Before getting a new word, always check that there is a word available by evaluating `ws.hasMoreTokens()`, which will return true if there are more words available. If you call `nextToken()` when there are no more words available (i.e., you’ve exhausted the input), then it will throw
an `IndexOutOfBoundsException`.


## Getting Started ##

1. Think carefully about the design of this program before sitting down at a computer. What would a reasonable `hashCode` method be for `StringPair`? What other methods and instance variables might the `FreqList` class need? For the `get` method in the `FreqList` class, take a piece of paper and create different examples of frequency lists. For each example, work out what the `get` method would return for different values of `p` (e.g., if the frequency list contained 4 words that each occurred once and *p* = *0.45*, what word would be returned?)

2.  You are now ready to get started!

   - We recommend starting with the `FreqList` class and its `main()` test cases.
   - Next we suggest implementing the `StringPair` class and `main()` test cases.
   - Next we suggest that you implement and test `TextGenerator.enter()`,
     followed by `TextGenerator.getNextWord()`. 
       - Note: You may find the `[.nextDouble()](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html)` method useful here. 
   - Only after all of these work should you work on the actual text
     generation in `TextGenerator.main()`.

Again, try and develop incrementally. That is, get one small piece working
and then move on to another piece.  Do not attempt to move on to the next
step until you are confident of the correctness of the code you wrote in
the previous step.

## Grading

You will be graded based on the following criteria:


| Criterion                                           | Points |
| :-------------------------------------------------- | :----- |
| `StringPair`                                        | 1      |
| `FreqList`                                          | 3      |
| `TextGenerator`                                     | 3      |
| Prints `FreqList`                                   | 2      |
| Prints at least 400 words of generated text         | 2      |
| Appropriate comments (including JavaDoc) +  General Correctness + Style and formatting    | 2      |
| **Total**                                           | **13** |
| Extra Credit                                        | 1      |


NOTE: Code that does not compile will not be accepted! Make sure that your code compiles before submitting it.

## Submitting your work

Double-check that your work is indeed pushed in Github and appears on Gradescope! It is your responsibility to ensure that you do so before the deadline. Don't forget to commit and push your changes as you go. 

## Extra credit

There are many ways in which you might extend such a program. For example, as described, word pairs
which never appeared in the input will never appear in the output. Is there a way you could introduce a bit
of "mutation" to allow new pairs to appear?
These "trigrams" of words are used in natural language processing in order to categorize kinds of articles
and their authors. Think about how you might use tables like those given here to help determine if two
articles are written by the same author.
In case you do attempt an extra credit extension, don’t forget to indicate in comments wherever you attempted extra credit.
