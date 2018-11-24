package com.dream.inventory.setting.impl.location

trait LocationComponentSupport { this: LocationComponent =>

  trait LocationDaoSupport { this: DaoSupport[String, LocationRecord] =>

  }


  trait LocationGroupDaoSupport { this: DaoSupport[String, LocationGroupRecord] =>

  }

}
