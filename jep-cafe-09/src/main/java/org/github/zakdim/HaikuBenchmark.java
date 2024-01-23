/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.github.zakdim;

import org.eclipse.collections.api.bag.primitive.CharBag;
import org.eclipse.collections.api.bag.primitive.MutableCharBag;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.set.primitive.CharSet;
import org.eclipse.collections.api.tuple.primitive.CharIntPair;
import org.eclipse.collections.impl.factory.Strings;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
//@Warmup(iterations = 3)
//@Measurement(iterations = 3)
@State(Scope.Benchmark)
public class HaikuBenchmark {

    String haiku = """
            Breaking Through                   Pavement                  Wakin' with Bacon     
            ----------------                   --------                  -----------------        
            The wall disappears                Beautiful pavement!       Wakin' with Bacon        
            As soon as you break through the   Imperfect path before me  On a Saturday morning    
            Intimidation                       Thank you for the ride    Life's little pleasures  
                            
            Homeward Found                     With Deepest Regrets
            --------------                     --------------------
            House is where I am                With deepest regrets
            Home is where I want to be         That which you have yet to write
            Both may be the same               At death, won't be wrote 
                            
            Winter Slip and Slide              Simple Nothings                
            ---------------------              ---------------                
            Run up the ladder                  A simple flower                
            Swoosh down the slide in the snow  Petals shine vibrant and pure    
            Winter slip ans slide              Stares into the void    
                            
            Caffeinated Coding Rituals  Finding Solace               Curious Cat
            --------------------------  --------------               -----------
            I arrange my desk,          Floating marshmallows        I see something move 
            refactor some ugly code,    Cocoa brewed hot underneath  What it is, I am not sure 
            and drink my coffee.        Comfort in a cup             Should I pounce or not?
                            
            Eleven
            ------
            This is how many
            Haiku I write before I
            Write a new tech blog.
            """;

//    @Benchmark
//    public void testMethod() {
//        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
//        // Put your benchmark code here.
//    }

    @Benchmark
    public String distinct_letters_EC() {
        String distinctLetters = Strings.asChars(this.haiku)
                .select(Character::isAlphabetic)
                .collectChar(Character::toLowerCase)
                .distinct()
                .toString();

        return distinctLetters;
    }

    @Benchmark
    public String distinct_letters_Java17() {
        String distinctLetters = this.haiku.chars()
                .filter(Character::isAlphabetic)
                .map(Character::toLowerCase)
                .distinct()
                .mapToObj(Character::toString)
                .collect(Collectors.joining());

        return distinctLetters;
    }


    @Benchmark
    public ListIterable<CharIntPair> top_letters_EC() {
        CharBag chars = Strings.asChars(this.haiku)
                .select(Character::isAlphabetic)
                .collectChar(Character::toLowerCase)
                .toBag();

        ListIterable<CharIntPair> top3 = chars.topOccurrences(3);
        return top3;
    }

    @Benchmark
    public Object top_letters_JS_V1() {
        Map<String, Long> chars =
                this.haiku.chars()
                        .filter(Character::isAlphabetic)
                        .map(Character::toLowerCase)
                        .mapToObj(Character::toString)
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        ));

        Map.Entry<String, Long> mostFrequentLetter =
                chars.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElseThrow();

        List<Map.Entry<String, Long>> mostFrequentLetters =
                chars.entrySet().stream()
                        .sorted(Map.Entry
                                .<String, Long>comparingByValue()
                                .reversed())
                        .limit(3)
                        .toList();

        return mostFrequentLetters;
    }

    @Benchmark
    public Object top_letters_JS_V2() {

        Map<Character, Long> chars =
                this.haiku.chars()
                        .filter(Character::isAlphabetic)
                        .map(Character::toLowerCase)
                        .mapToObj(i -> (char) i)
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        ));

        Map<Long, List<Character>> map =
                chars.entrySet().stream()
                        .collect(Collectors.groupingBy(
                                Map.Entry::getValue,
                                Collectors.mapping(
                                        Map.Entry::getKey,
                                        Collectors.toList()
                                )
                        ));

        Map.Entry<Long, List<Character>> mostSeenLetter =
                map.entrySet().stream()
                        .max(Map.Entry.comparingByKey())
                        .orElseThrow();

        List<Map.Entry<Long, List<Character>>> mostSeenLetters =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                        .limit(3)
                        .toList();

        return mostSeenLetters;
    }

    // V1
//    record Letter(int codePoint) {
//        Letter(int codePoint) {
//            this.codePoint = Character.toLowerCase(codePoint);
//        }
//
//        @Override
//        public String toString() {
//            return "Letter[" +
//                    "codePoint=" + codePoint +
//                    ", char=" + (char) codePoint +
//                    ']';
//        }
//    }

    // V2
    record Letter(int codePoint) {
        Letter(int codePoint) {
            if (!Character.isAlphabetic(codePoint)) {
                throw new IllegalArgumentException("Letter is build on letters");
            }
            this.codePoint = Character.toLowerCase(codePoint);
        }
    }

    // V1
//    record LetterCount(long count) implements Comparable<LetterCount> {
//        @Override
//        public int compareTo(LetterCount other) {
//            return Long.compare(this.count, other.count);
//        }
//    }

    // V2
    record LetterCount(long count) implements Comparable<LetterCount> {
        @Override
        public int compareTo(LetterCount other) {
            return Long.compare(this.count, other.count);
        }

        static Collector<Letter, Object, LetterCount> counting() {
            return Collectors.collectingAndThen(Collectors.counting(), LetterCount::new);
        }
    }


    // V1
//    record LetterByCount(Letter letter, LetterCount count) {
//        LetterByCount(Map.Entry<Letter, LetterCount> entry) {
//            this(entry.getKey(), entry.getValue());
//        }
//    }

    // V2
    record LetterByCount(Letter letter, LetterCount count) {
        LetterByCount(Map.Entry<Letter, LetterCount> entry) {
            this(entry.getKey(), entry.getValue());
        }

        public boolean isNotUnique() {
            return count.count() > 1L;
        }

        public boolean isUnique() {
            return count.count() == 1L;
        }
    }



    record LettersByCount(LetterCount count, List<Letter> letters) {
        LettersByCount(Map.Entry<LetterCount, List<Letter>> entry) {
            this(entry.getKey(), entry.getValue());
        }

        public static Comparator<? super LettersByCount> comparingByCount() {
            return Comparator.comparing(LettersByCount::count);
        }
    }

    @Benchmark
    public List<LettersByCount> top_letters_JS_V3() {
        Map<Letter, LetterCount> chars = this.haiku.chars()
                .filter(Character::isAlphabetic)
                .mapToObj(Letter::new)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                LetterCount::new
                        )));

        Map<LetterCount, List<Letter>> map =
                chars.entrySet().stream()
                        .map(LetterByCount::new)
                        .collect(Collectors.groupingBy(
                                LetterByCount::count,
                                Collectors.mapping(LetterByCount::letter, Collectors.toList())
                        ));

        LettersByCount mostSeenLetter =
                map.entrySet().stream()
                        .map(LettersByCount::new)
                        .max(LettersByCount.comparingByCount())
                        .orElseThrow();

        List<LettersByCount> mostSeenLetters =
                map.entrySet().stream()
                        .map(LettersByCount::new)
                        .sorted(LettersByCount.comparingByCount().reversed())
                        .limit(3)
                        .toList();

        return mostSeenLetters;
    }

    @Benchmark
    public Object duplicates_and_unique_EC() {
        MutableCharBag chars = Strings.asChars(this.haiku)
                .select(Character::isAlphabetic)
                .collectChar(Character::toLowerCase)
                .toBag();

        CharBag duplicates = chars.selectDuplicates();
        CharSet unique = chars.selectUnique();

        return List.of(duplicates, unique);
    }

    @Benchmark
    public Object duplicates_and_unique_JS() {
        Map<Letter, LetterCount> lettersByNumber =
                this.haiku.chars()
                        .mapToObj(Letter::new)
                        .collect(Collectors.groupingBy(Function.identity(), LetterCount.counting()));

        Set<Letter> duplicates =
                lettersByNumber.entrySet().stream()
                        .map(LetterByCount::new)
                        .filter(LetterByCount::isNotUnique)
                        .map(LetterByCount::letter)
                        .collect(Collectors.toSet());

        Set<Letter> unique =
                lettersByNumber.entrySet().stream()
                        .map(LetterByCount::new)
                        .filter(LetterByCount::isUnique)
                        .map(LetterByCount::letter)
                        .collect(Collectors.toSet());


        return List.of(duplicates, unique);
    }


    public static void main(String[] args) {
        HaikuBenchmark haikuBenchmark = new HaikuBenchmark();
        var topLettersEC = haikuBenchmark.top_letters_EC();
        var topLettersJSV1 = haikuBenchmark.top_letters_JS_V1();
        var topLettersJSV2 = haikuBenchmark.top_letters_JS_V2();
        var topLettersJSV3 = haikuBenchmark.top_letters_JS_V3();

        System.out.println(topLettersEC);
        System.out.println(topLettersJSV1);
        System.out.println(topLettersJSV2);
        topLettersJSV3.forEach(System.out::println);
    }
}
