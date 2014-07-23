/*
 * =========================================================================================
 * Copyright © 2013 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */

package kamon.newrelic

import akka.actor.Actor
import kamon.metric._

trait CustomMetrics {
  self: Actor ⇒

  def collectCustomMetrics(metrics: Map[MetricGroupIdentity, MetricGroupSnapshot]): Seq[NewRelic.Metric] = {
    metrics.collect {
      case (UserMetrics, groupSnapshot) ⇒
        groupSnapshot.metrics collect {
          case (name, snapshot) ⇒ toNewRelicMetric(Scale.Unit)(s"Custom/$name", None, snapshot)
        }
    }.flatten.toSeq
  }
}