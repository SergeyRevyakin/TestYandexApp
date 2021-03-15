package ru.serg.testyandexapp.data

import com.github.mikephil.charting.data.Entry

class GraphHistoryItem(
    price: Float,
    date: Float
):Entry(date, price)