package com.example.data.model

import com.example.R

enum class EpochType(
    val order: Int,
    val title: String,
    val scientificName: String,
    val timePeriod: String,
    val brainVolume: String,
    val location: String,
    val imageRes: Int,
    val avatarRes: Int,
    val iconEmoji: String,
    val nextEpochTargetEvolution: Int,
    val keyFact: String
) {
    DRYOPITHECUS(
        order = 1,
        title = "Дріопітек",
        scientificName = "Dryopithecus (Деревна мавпа)",
        timePeriod = "Близько 4 млн. років тому",
        brainVolume = "300 – 350 см³",
        location = "Густі тропічні праліси та рідколісся Африки та Європи",
        imageRes = R.drawable.dryopithecus_avatar,
        avatarRes = R.drawable.dryopithecus_avatar,
        iconEmoji = "🌿",
        nextEpochTargetEvolution = 100,
        keyFact = "Дріопітек жив на деревах і харчувався плодами. Зміна клімату змусила його спускатися на землю та підніматися на дві лапи."
    ),
    AUSTRALOPITHECUS(
        order = 2,
        title = "Австралопітек",
        scientificName = "Australopithecus (Південна мавпа)",
        timePeriod = "3 – 2 млн. років тому",
        brainVolume = "450 – 550 см³",
        location = "Східна та Південна Африка (Савана)",
        imageRes = R.drawable.img_ancestors,
        avatarRes = R.drawable.avatar_australopithecus,
        iconEmoji = "🦴",
        nextEpochTargetEvolution = 250,
        keyFact = "Австралопітек (знаменита 'Люсі') вже впевнено ходив на двох ногах. Руки звільнилися для збирання їжі та каміння."
    ),
    HOMO_ERECTUS(
        order = 3,
        title = "Людина прямоходяча",
        scientificName = "Homo erectus (Пітекантроп)",
        timePeriod = "1.5 млн – 300 тис. років тому",
        brainVolume = "900 – 1100 см³",
        location = "Африка, Європа (стоянка Королево на Закарпатті), Азія",
        imageRes = R.drawable.img_fire_era,
        avatarRes = R.drawable.avatar_homo_erectus,
        iconEmoji = "🔥",
        nextEpochTargetEvolution = 500,
        keyFact = "Першим приборкав вогонь і створив кам'яне рубило. Найдавніша стоянка в Україні — с. Королево (1.4 млн р.т.)."
    ),
    HOMO_NEANDERTHALENSIS(
        order = 4,
        title = "Неандерталець",
        scientificName = "Homo neanderthalensis",
        timePeriod = "300 – 35 тис. років тому",
        brainVolume = "1400 – 1600 см³",
        location = "Льодовикова Європа, печери, стоянки Киїк-Коба (Крим)",
        imageRes = R.drawable.img_ice_age,
        avatarRes = R.drawable.avatar_neanderthal,
        iconEmoji = "❄️",
        nextEpochTargetEvolution = 850,
        keyFact = "Жив у суворий Льодовиковий період, носив одяг зі шкур, будував житла з кісток мамонтів та ховав померлих родичів."
    ),
    HOMO_SAPIENS(
        order = 5,
        title = "Людина розумна",
        scientificName = "Homo sapiens (Кроманьйонець)",
        timePeriod = "40 – 10 тис. років тому",
        brainVolume = "1350 – 1450 см³",
        location = "По всій планеті (в Україні — стоянки Мізин, Межиріч)",
        imageRes = R.drawable.img_civilization,
        avatarRes = R.drawable.avatar_homo_sapiens,
        iconEmoji = "🏹",
        nextEpochTargetEvolution = 1200,
        keyFact = "Створив членороздільну мову, родове суспільство, лук і стріли, списокидалку та перше мистецтво (малюнки, музика)."
    );

    companion object {
        fun fromOrder(order: Int): EpochType {
            return entries.find { it.order == order } ?: DRYOPITHECUS
        }
    }
}
