package buildresults.diode

import diode.Action
import buildresults.models.{PageContent}

case class AppState (
                      pageContent: Option[PageContent],
                      isLoading: Boolean
                    )

case class AppModel(
                     state: AppState
                   )


case class GetPageContent(content: Option[PageContent]) extends Action

case class SetLoadingState() extends Action

case class ClearLoadingState() extends Action


