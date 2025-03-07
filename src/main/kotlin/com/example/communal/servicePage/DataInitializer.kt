package com.example.communal.servicePage

import com.example.communal.servicePage.model.ServiceEntity
import com.example.communal.servicePage.repository.ServiceRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DataInitializer(private val serviceRepository: ServiceRepository) {

    @PostConstruct
    fun initDefaultServices() {
        if (serviceRepository.count() == 0L) {
            val defaultServices = listOf(
                ServiceEntity(
                    name = "Проверка счетчиков воды",
                    category = "Водоснабжение",
                    description = "Проверка правильности работы счетчиков холодной и горячей воды",
                    example = "Пример: диагностика состояния счетчика",
                    price = BigDecimal("150.00")
                ),
                ServiceEntity(
                    name = "Установка счетчика воды",
                    category = "Водоснабжение",
                    description = "Установка нового счетчика воды",
                    example = "Пример: установка в новом доме",
                    price = BigDecimal("1000.00")
                ),
                ServiceEntity(
                    name = "Проверка счетчика газа",
                    category = "Газоснабжение",
                    description = "Диагностика показаний и состояния счетчика газа",
                    example = "Пример: диагностика утечек",
                    price = BigDecimal("200.00")
                ),
                ServiceEntity(
                    name = "Установка газового счетчика",
                    category = "Газоснабжение",
                    description = "Установка нового газового счетчика",
                    example = "Пример: установка газового прибора",
                    price = BigDecimal("1500.00")
                ),
                ServiceEntity(
                    name = "Проверка счетчика электроэнергии",
                    category = "Электроснабжение",
                    description = "Диагностика состояния счетчика электроэнергии",
                    example = "Пример: диагностика работы счетчика",
                    price = BigDecimal("100.00")
                ),
                ServiceEntity(
                    name = "Установка счетчика электроэнергии",
                    category = "Электроснабжение",
                    description = "Установка нового счетчика электроэнергии",
                    example = "Пример: установка в квартире",
                    price = BigDecimal("800.00")
                ),
                ServiceEntity(
                    name = "Обслуживание системы отопления",
                    category = "Отопление",
                    description = "Профилактическое обслуживание системы отопления",
                    example = "Пример: проверка котла и труб",
                    price = BigDecimal("500.00")
                ),
                ServiceEntity(
                    name = "Ремонт системы отопления",
                    category = "Отопление",
                    description = "Ремонт неисправностей в системе отопления",
                    example = "Пример: ремонт труб",
                    price = BigDecimal("1200.00")
                ),
                ServiceEntity(
                    name = "Уборка подъезда",
                    category = "Уборка и обслуживание",
                    description = "Ежемесячная уборка общего пространства подъезда",
                    example = "Пример: уборка коридора и лестницы",
                    price = BigDecimal("300.00")
                ),
                ServiceEntity(
                    name = "Вывоз мусора",
                    category = "Уборка и обслуживание",
                    description = "Организация вывоза мусора 2 раза в месяц",
                    example = "Пример: регулярный вывоз",
                    price = BigDecimal("400.00")
                )
            )
            serviceRepository.saveAll(defaultServices)
        }
    }
}
