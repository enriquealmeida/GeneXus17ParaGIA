# Design System Tokens

A token is the most basic element of a Design System that allows you to capture an option for visual or interaction design.
A token allows you to model a – platform-agnostic– choice of color, typography, spacing, time, media, zindex, border, opacity, size.

With tokens, you can give options for low-level values of your product.

Having a Token set for your Design System will allow your product to be more maintainable and consistent in terms of design.

## Getting Started

Clone this repo and then:

```
npm install
npm start
```

## Building for production

```
npm run build
```

## Token Definition Basic Sample

```css
tokens MyDesignSystemTokens {
  #colors {
    primary: rgb(108, 154, 235);
    secondary: #ab2;
    background: rgba(200, 200, 150, 0.9);
  }
  #spacing {
    small: 12px;
    medium: 22px;
    large: 45px;
  }
  #fonts {
    primary: arial;
    secondary: georgia;
    third: roboto;
  }
  #fontsizes {
    xsmall: 5px;
    small: 10px;
    regular: 22px;
    big: 50px;
  }
  #zindex {
    high: 5;
  }
  #borders {
    hard: 15px;
    medium: 5px;
    thin: 1px;
    none: 0px;
  }
  #radius {
    strict: 0px;
    soft: 10px;
    regular: 16px;
    medium: 22px;
    big: 34px;
  }
  #times {
    snail: 3s;
    rabbit: 1s;
    horse: 0.3s;
  }
}
```

## Token Set

Allow grouping a set of tokens.

```javascript
tokens MyDesignTokens
{
  // Here define tokens
}
```

See more on [Token Sets](TokenSets.md)

## Token Types

A specific Token Set includes several tokens defined by Types.

There is a set of predefined token types. Having Token Types allow giving a clear meaning of the kind of token we are creating.

Readers can have a clear understanding of what context this token can be used, and tools can give different ways of editing or showing these tokens.

Predefined token types are:

```css
#colors
#spacing
#borders
#opacity
#radius
#shadows
#times  (Use timing tokens for animation durations.)
#fonts
#fontsizes
#mediaqueries
#zindex
```

### Sample

```css
tokens MyDesignTokens {
  #colors {
    active: red;
    text: #FFFFF;
  }
}
```

See more on [Token Types](TokenTypes.md)

## Tokens

For each Token Type inside a Token Set we can define Tokens.

Each Token has at least a [Token Name](TokenName.md) and a [Token Value](TokenValue.md)

Depending on the Token Type is the kind of Token Value allowed.

The syntax for the two required elements is equal to a css property, the only difference is that validators should take into account that the domain for values has constraints depending on the Token Type group.

### Sample

```css
tokens MyDesignSystemTokens {
  #spacing {
    small: 12px;
    big: 45px;
  }
  #colors {
    active: red;
    text: #ffffff;
  }
}
```

See more on [Tokens](Tokens.md)

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
