package ru.mfp.config

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.Currency

@Converter(autoApply = true)
class CurrencyConverter : AttributeConverter<Currency, String> {

    override fun convertToDatabaseColumn(attribute: Currency?) = attribute?.currencyCode

    override fun convertToEntityAttribute(dbData: String?): Currency? = Currency.getInstance(dbData)
}
