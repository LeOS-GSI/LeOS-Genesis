/**
 * Copyright 2017 Carlos Macasaet
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hiddenservices.onionservices.libs.fernet;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.util.function.Function;

public interface StringObjectValidator<T> extends Validator<T> {

    default Charset getCharset() {
        return UTF_8;
    }

    /**
     * Override this to specify an alternative way to convert binary data into a String. The default implementation uses
     * the UTF-8 character set.
     *
     * @return a method for converting a byte array into a String
     */
    default Function<byte[], String> getStringCreator() {
        return bytes -> new String(bytes, getCharset());
    }


    /**
     * Plug in your String deserialisation method here.
     *
     * @return a method for converting a String into an Object.
     */
    Function<String, T> getStringTransformer();

}