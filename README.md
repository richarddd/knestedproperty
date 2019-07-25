# knestedproperty [![Travis Build](https://api.travis-ci.com/richarddd/knestedproperty.svg?branch=master)](https://travis-ci.com/richarddd/knestedproperty)
Nested property util for kotlin. This extention methods allows you to get names and values of nested propertys in a typesafe manner.

## Installation

TODO

## Usage

  ```kotlin
  class Foo {
    val bar = Bar()
  }

  class Bar {
      val value = "FooBar"
  }
  ```
  ```kotlin
  println((Foo::bar / Bar::value).name) //prints: bar.value
  
  val foo = Foo()
  println((Foo::bar / Bar::value).value(foo) //prints: "FooBar"
  ```


