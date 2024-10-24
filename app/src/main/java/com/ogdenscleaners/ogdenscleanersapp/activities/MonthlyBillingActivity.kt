package com.ogdenscleaners.ogdenscleanersapp.activities

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ogdenscleaners.ogdenscleanersapp.R
import com.ogdenscleaners.ogdenscleanersapp.adapters.BillingAdapter
import com.ogdenscleaners.ogdenscleanersapp.models.BillingStatement

class MonthlyBillingActivity() : AppCompatActivity(), Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MonthlyBillingActivity> {
        override fun createFromParcel(parcel: Parcel): MonthlyBillingActivity {
            return MonthlyBillingActivity(parcel)
        }

        override fun newArray(size: Int): Array<MonthlyBillingActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_billing)

        // Placeholder data for billing statements
        val oneHundred = "100.00"
        val billingStatements = listOf(
            BillingStatement(
                id = "1",
                month = "January 2024",
                totalAmount = "$100.00",
                totalAmountText = oneHundred,
                details = "Monthly dry cleaning charges",
                paidStatus = false
            ),
            BillingStatement(
                id = "2",
                month = "February 2024",
                totalAmount = "$80.00",
                totalAmountText = oneHundred,
                details = "Monthly dry cleaning charges",
                paidStatus = true
            )
        )

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBilling)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BillingAdapter(billingStatements)
    }
}
