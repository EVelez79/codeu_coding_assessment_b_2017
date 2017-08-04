// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.*;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {
  private StringBuilder tokenBeingRead = new StringBuilder();
  private String source;
  private int index = 0;


  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
  }

  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.
    while (remainingChars() > 0 && Character.isWhitespace(peek())) {
      read();
    }

    if (remainingChars() <= 0) {
      return null;
    } else if (peek() == '"') {
      return checkToken(readWithQuotes());
    } else {
      return checkToken(readWithoutQuotes());
    }
  }

  private int remainingChars() {
    return source.length() - index;
  }

  private char peek() throws IOException {
    if (index < source.length()) {
      return source.charAt(index);
    } else {
      throw new IOException("Error during call on peek method.");
    }
  }

  private char read() throws IOException {
    final char c = peek();
    index += 1;
    return c;
  }

  private String readWithQuotes() throws IOException {
    tokenBeingRead.setLength(0);
    if (read() != '"') {
      throw new IOException("Strings must start with quote.");
    }
    while (peek() != '"') {
      tokenBeingRead.append(read());
    }
    read();
    System.out.println(tokenBeingRead.toString());
    return tokenBeingRead.toString();
  }

  private String readWithoutQuotes() throws IOException {
    tokenBeingRead.setLength(0);

    while (remainingChars() > 0 && !Character.isWhitespace(peek())) {
      if (peek() == ';' && tokenBeingRead.length() > 0) {
        return tokenBeingRead.toString();
      } else {
        tokenBeingRead.append(read());
      }
    }

    return tokenBeingRead.toString();
  }

  private Token checkToken(String token) throws IOException {
    if (token.length() == 1) {
        if (checkIfLetter(token)) {
          NameToken name = new NameToken(token);
          return name;
        } else if (checkIfDigit(token)) {
          NumberToken number = new NumberToken(Double.parseDouble(token));
          return number;
        } else {
          return getSymbol(token);
        }
      } else {
        switch (token) {
          case "let":
            NameToken letFunctionToken = new NameToken(token);
            return letFunctionToken;
          case "note":
            NameToken noteFunctionToken = new NameToken(token);
            return noteFunctionToken;
          case "print":
            NameToken printFunctionToken = new NameToken(token);
            return printFunctionToken;
          default:
            StringToken stringToken = new StringToken(token);
            return stringToken;
        }
      }
  }

  private boolean checkIfLetter(String token) {
    return Character.isLetter(token.charAt(0));
  }

  private boolean checkIfDigit(String token) {
    return Character.isDigit(token.charAt(0));
  }

  private Token getSymbol(String charToken) {
    switch (charToken.charAt(0)) {
      case '=':
        SymbolToken equalSymbol = new SymbolToken(charToken.charAt(0));
        return equalSymbol;
      case '+':
        SymbolToken additionSymbol = new SymbolToken(charToken.charAt(0));
        return additionSymbol;
      case '-':
        SymbolToken subtractionSymbol = new SymbolToken(charToken.charAt(0));
        return subtractionSymbol;
      default:
        StringToken stringToken = new StringToken(charToken);
        return stringToken;
      }
    }
}
