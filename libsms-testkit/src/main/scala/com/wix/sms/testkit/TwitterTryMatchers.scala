/*      __ __ _____  __                                              *\
**     / // // /_/ |/ /          Wix                                 **
**    / // // / /|   /           (c) 2006-2015, Wix LTD.             **
**   / // // / //   |            http://www.wix.com/                 **
**   \__/|__/_//_/| |                                                **
\*                |/                                                 */
package com.wix.sms.testkit

import com.twitter.util.{Throw, Try}
import org.specs2.matcher._

import scala.reflect.ClassTag


trait TwitterTryMatchers { this: Matchers =>
  def beSuccessful[T](value: Matcher[T] = AlwaysMatcher()): Matcher[Try[T]] = {
    SuccessMatcher[T]() and
      value ^^ { (_: Try[T]).get() }
  }

  def beFailure[T, E <: Throwable : ClassTag](msg: Matcher[String] = AlwaysMatcher(),
                                              cause: Matcher[Throwable] = AlwaysMatcher()): Matcher[Try[T]] = {
    TwitterTryMatchers.FailureMatcher[T]() and
      beAnInstanceOf[E] ^^ { (_: Try[T]) match {
        case Throw(e) => e
        case _ => null
      } } and
      new Matcher[Try[T]] {
        override def apply[S <: Try[T]](expectable: Expectable[S]): MatchResult[S] = {
          expectable.value match {
            case Throw(e) => createExpectable(e.getMessage).applyMatcher(msg).asInstanceOf[MatchResult[S]]
            case _ => failure("Expected a failure (exception), but was successful", expectable)
          }
        }
      } and
      new Matcher[Try[T]] {
        override def apply[S <: Try[T]](expectable: Expectable[S]): MatchResult[S] = {
          expectable.value match {
            case Throw(e) => createExpectable(e.getCause).applyMatcher(cause).asInstanceOf[MatchResult[S]]
            case _ => failure("Expected a failure (exception), but was successful", expectable)
          }
        }
      }
  }

  case class SuccessMatcher[T]() extends OptionLikeMatcher[Try, T, T]("a Success", (_: Try[T]).toOption)

  case class FailureMatcher[T]() extends OptionLikeMatcher[Try, T, Throwable](
    "a Failure",
    (_: Try[T]) match {
      case Throw(e) => Option(e)
      case _ => None
    })
}

object TwitterTryMatchers extends TwitterTryMatchers with Matchers