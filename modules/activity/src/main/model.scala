package lila.activity

import ornicar.scalalib.Zero

object model {

  case class Rating(value: Int) extends AnyVal
  case class RatingProg(before: Rating, after: Rating) {
    def +(o: RatingProg) = copy(after = o.after)
    def diff = after.value - before.value
    def isEmpty = diff == 0
  }
  object RatingProg {
    def +(rp1O: Option[RatingProg], rp2O: Option[RatingProg]) = (rp1O, rp2O) match {
      case (Some(rp1), Some(rp2)) => Some(rp1 + rp2)
      case _ => rp2O orElse rp1O
    }
    def make(player: lila.game.Player) = player.rating map { rating =>
      RatingProg(Rating(rating), Rating(rating + ~player.ratingDiff))
    }
  }

  case class Score(win: Int, loss: Int, draw: Int, rp: Option[RatingProg]) {
    def +(s: Score) = copy(
      win = win + s.win,
      loss = loss + s.loss,
      draw = draw + s.draw,
      rp = RatingProg.+(rp, s.rp)
    )
    def size = win + loss + draw
  }
  object Score {
    def make(res: Option[Boolean], rp: Option[RatingProg]): Score = Score(
      win = res.has(true) ?? 1,
      loss = res.has(false) ?? 1,
      draw = res.isEmpty ?? 1,
      rp = rp
    )
    def make(povs: List[lila.game.Pov]): Score = povs.foldLeft(ScoreZero.zero) {
      case (score, pov) if pov.game.finished => score + make(
        res = pov.game.wonBy(pov.color),
        rp = RatingProg.make(pov.player)
      )
    }
  }
  implicit val ScoreZero = Zero.instance(Score(0, 0, 0, none))

  case class GameId(value: String) extends AnyVal
}
