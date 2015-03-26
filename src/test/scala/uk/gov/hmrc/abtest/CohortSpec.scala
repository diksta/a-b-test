/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.abtest

import org.scalatest.{WordSpec, Matchers}

class CohortSpec extends WordSpec with Matchers {

  "Cohort calculator" should {
    object AnotherCohort extends Cohort {
      val id = 1
      val name = "cohort"
    }

    val id = "an id"
    val anotherId = "another"
    val differentId = 1234

    val cohort = new Cohort() {}
    val anotherCohort = new Cohort() {}

    "throw exception when no cohorts are supplied" in new CohortCalculator[Cohort] {
      val values = Set.empty[Cohort]
      intercept[ArithmeticException] {
        calculate(id)
      }
     }

     "return only available cohort when only one specified" in new CohortCalculator[Cohort] {
       val values = Set[Cohort](cohort)
       calculate(id) should be (cohort)
     }

    "return matching cohort for given id" in new CohortCalculator[Cohort] {
      val values = Set[Cohort](cohort, anotherCohort)
      calculate(id) should be (cohort)
      calculate(anotherId) should be (anotherCohort)
    }

    "return matching cohort for an id of any type" in new CohortCalculator[Cohort] {
      val values = Set[Cohort](cohort)
      calculate(differentId) should be (cohort)
    }

    "return matching typed cohort" in new CohortCalculator[AnotherCohort.type] {
      val values = Set(AnotherCohort)
      calculate(differentId).id should be (1)
    }
  }
}