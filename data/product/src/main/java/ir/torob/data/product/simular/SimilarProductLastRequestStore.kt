package ir.torob.data.product.simular

import ir.torob.data.model.Request
import ir.torob.data.product.lastrequests.EntityLastRequestStore
import ir.torob.db.dao.LastRequestDao
import javax.inject.Inject

class SimilarProductLastRequestStore @Inject constructor(
    dao: LastRequestDao,
) : EntityLastRequestStore(Request.SIMILAR_PRODUCT, dao)