package lila.report

import lila.user.User

case class Mod(user: User) extends AnyVal

case class ModId(value: User.ID) extends AnyVal
object ModId {
  def lichess = ModId("lichess")
  def irwin = ModId("irwin")
  def normalize(username: String) = ModId(User normalize username)
}

case class Suspect(user: User) extends AnyVal {
  def set(f: User => User) = copy(user = f(user))
}
case class SuspectId(value: User.ID) extends AnyVal
object SuspectId {
  def normalize(username: String) = SuspectId(User normalize username)
}

case class Victim(user: User) extends AnyVal

case class Reporter(user: User) extends AnyVal {
  def id = ReporterId(user.id)
}
case class ReporterId(value: User.ID) extends AnyVal

object ReporterId {
  def lichess = ReporterId("lichess")
  def irwin = ReporterId("irwin")
  implicit val reporterIdIso = lila.common.Iso.string[ReporterId](ReporterId.apply, _.value)
}

case class Accuracy(value: Int) extends AnyVal
