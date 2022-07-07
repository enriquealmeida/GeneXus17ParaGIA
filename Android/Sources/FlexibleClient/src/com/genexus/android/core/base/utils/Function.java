package com.genexus.android.core.base.utils;

/**
 * Determines an output value based on an input value (acts as a Parameterized Runnable).
 * Similar to http://code.google.com/p/guava-libraries/wiki/FunctionalExplained
 *
 * @param <InputT> Type of the function input.
 * @param <OutputT> Type of the function output.
 */
public interface Function<InputT, OutputT>
{
	OutputT run(InputT input);
}
