/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.bench.java.util.regex;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class PatternBench {

    public String fileTestString;
    public String flagsString;


    public Pattern graphemePattern;
    public Pattern jmodPattern;
    public Pattern jmodCanonicalPattern;

    public Pattern pattern;

    @Setup
    public void setup() {
        flagsString = "\ud83c\udde6\ud83c\uddec\ud83c\uddec\ud83c\udde6\ud83c\uddfa\ud83c\uddf8\ud83c\uddeb\ud83c\uddf7";
        fileTestString = "META-INF/providers/org.openjdk.foo_hotspot_nodes_PluginFactory_EndLockScopeNode";
        graphemePattern = Pattern.compile("\\b{g}");

        String jmodRegex = "^.*(?:(?:_the\\.[^/]*)|(?:_[^/]*\\.marker)|(?:[^/]*\\.diz)|(?:[^/]*\\.debuginfo)|(?:[^/]*\\.dSYM/.*)|(?:[^/]*\\.dSYM)|(?:[^/]*\\.pdb)|(?:[^/]*\\.map))$";
        jmodCanonicalPattern = Pattern.compile(jmodRegex, Pattern.CANON_EQ);
        jmodPattern = Pattern.compile(jmodRegex);
    }

    @Benchmark
    @Warmup(iterations = 3)
    @Measurement(iterations = 3)
    public int splitFlags() {
        return graphemePattern.split(flagsString).length;
    }

    @Benchmark
    @Warmup(iterations = 3)
    @Measurement(iterations = 3)
    public boolean canonicalJmodMatch() {
        return jmodCanonicalPattern.matcher(fileTestString).matches();
    }

    @Benchmark
    @Warmup(iterations = 3)
    @Measurement(iterations = 3)
    public boolean normalJmodMatch() {
        return jmodPattern.matcher(fileTestString).matches();
    }
}
