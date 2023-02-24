package ir.torob.data.product.detail

import ir.torob.data.model.Request
import ir.torob.data.product.lastrequests.EntityLastRequestStore
import ir.torob.db.dao.LastRequestDao
import javax.inject.Inject

class ProductDetailLastRequestStore @Inject constructor(
    dao: LastRequestDao,
) : EntityLastRequestStore(Request.PRODUCT_DETAIL, dao)