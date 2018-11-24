package controllers

import com.dream.inventory.api.SettingService
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

class UoMController (settingService: SettingService, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractAuctionController(controllerComponents) {


}
