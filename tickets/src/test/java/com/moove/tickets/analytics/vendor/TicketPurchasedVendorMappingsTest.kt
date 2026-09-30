package com.moove.tickets.analytics.vendor

import com.moove.tickets.analytics.event.TicketPurchased
import com.moove.tickets.analytics.vendor.adjust.toAdjustRevenueEvent
import com.moove.tickets.analytics.vendor.amplitude.toAmplitudeRevenueEvent
import com.moove.tickets.analytics.vendor.appsflyer.toAppsFlyerPurchaseEvent
import io.github.mkhytarmkhoian.herald.adjust.toAdjustEvent
import io.github.mkhytarmkhoian.herald.amplitude.toAmplitudeRevenue
import io.github.mkhytarmkhoian.herald.appsflyer.toAppsFlyerEventValues
import org.junit.Test
import kotlin.test.assertEquals

class TicketPurchasedVendorMappingsTest {

    private val purchase = TicketPurchased(
        TicketPurchased.Params(
            ryderId = "r1", fare = "1 Day Pass", count = 2, revenue = 10.0, currency = "USD", transactionId = "txn-1",
        )
    )

    @Test
    fun `given a purchase when mapped for Adjust then revenue, currency and the transaction id ride on the tokened event`() {
        val adjustEvent = purchase.toAdjustRevenueEvent().toAdjustEvent("abc123")

        assertEquals(10.0, adjustEvent.revenue)
        assertEquals("USD", adjustEvent.currency)
        assertEquals("txn-1", adjustEvent.deduplicationId)
        assertEquals("1 Day Pass", adjustEvent.callbackParameters["fare"])
    }

    @Test
    fun `given a purchase when mapped for AppsFlyer then af_purchase gets af_revenue and the order id, with the event's own values`() {
        val values = purchase.toAppsFlyerPurchaseEvent().toAppsFlyerEventValues()

        assertEquals(10.0, values["af_revenue"])
        assertEquals("USD", values["af_currency"])
        assertEquals(2, values["af_quantity"])
        assertEquals("1 Day Pass", values["af_content_id"])
        assertEquals("txn-1", values["af_order_id"])
        assertEquals("r1", values["ryder_id"])
    }

    @Test
    fun `given a purchase when mapped for Amplitude then the unit price, quantity and total reach the revenue API`() {
        val mapped = purchase.toAmplitudeRevenueEvent()
        val revenue = mapped.toAmplitudeRevenue()

        assertEquals<Double?>(5.0, revenue.price)
        assertEquals(2, revenue.quantity)
        assertEquals<Double?>(10.0, revenue.revenue)
        assertEquals("1 Day Pass", revenue.productId)
        assertEquals("txn-1", mapped.insertId)
    }

    @Test
    fun `given any mapping then it keeps the event's name and parameters`() {
        assertEquals(purchase.name, purchase.toAdjustRevenueEvent().name)
        assertEquals(purchase.name, purchase.toAppsFlyerPurchaseEvent().name)
        assertEquals(purchase.name, purchase.toAmplitudeRevenueEvent().name)
        assertEquals(purchase.parameters, purchase.toAdjustRevenueEvent().parameters)
        assertEquals(purchase.parameters, purchase.toAppsFlyerPurchaseEvent().parameters)
        assertEquals(purchase.parameters, purchase.toAmplitudeRevenueEvent().parameters)
    }
}
