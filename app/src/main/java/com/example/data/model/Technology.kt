package com.example.data.model

enum class TechCategory(val title: String, val iconEmoji: String) {
    TOOLS("Знаряддя та Зброя", "🪓"),
    SURVIVAL("Виживання та Вогонь", "🔥"),
    BIOLOGY("Анатомія та Мислення", "🧠"),
    SOCIETY_CULTURE("Суспільство та Мистецтво", "🎨"),
    AGRICULTURE("Господарство та Ремесла", "🌾")
}

data class Technology(
    val id: String,
    val name: String,
    val epoch: EpochType,
    val category: TechCategory,
    val costEvolution: Int,
    val costMaterials: Int,
    val prerequisiteId: String? = null,
    val iconEmoji: String,
    val shortEffect: String,
    val historicalDescription: String,
    val foodBonus: Int = 0,
    val materialBonus: Int = 0,
    val warmthBonus: Int = 0,
    val moraleBonus: Int = 0,
    val maxPopBonus: Int = 0
)

object TechnologyCatalog {
    val allTechnologies: List<Technology> = listOf(
        // Епоха 1: Дріопітек
        Technology(
            id = "tech_brachiation",
            name = "Брахіація (гойдання на руках)",
            epoch = EpochType.DRYOPITHECUS,
            category = TechCategory.BIOLOGY,
            costEvolution = 25,
            costMaterials = 10,
            iconEmoji = "🌴",
            shortEffect = "+5 Їжі від збирачів, рухливість плечей",
            historicalDescription = "Пересування на передніх кінцівках у кронах дерев сформувало широку грудну клітку та гнучкі суглоби зап'ястка.",
            foodBonus = 5
        ),
        Technology(
            id = "tech_tree_nesting",
            name = "Гнізда на деревах для ночівлі",
            epoch = EpochType.DRYOPITHECUS,
            category = TechCategory.SURVIVAL,
            costEvolution = 35,
            costMaterials = 15,
            iconEmoji = "🪺",
            shortEffect = "+10 Безпеки, захист від наземних хижаків",
            historicalDescription = "Складання гнучких гілок у зручні платформи на верхівках дерев забезпечувало безпечний сон зграї.",
            warmthBonus = 10
        ),
        Technology(
            id = "tech_natural_sticks",
            name = "Використання природних палиць",
            epoch = EpochType.DRYOPITHECUS,
            category = TechCategory.TOOLS,
            costEvolution = 50,
            costMaterials = 20,
            iconEmoji = "🦯",
            shortEffect = "+8 Матеріалів, викопування соковитих корінців",
            historicalDescription = "Використання звичайних сучків для добування комах та коріння стало першим кроком до знарядь.",
            materialBonus = 8
        ),

        // Епоха 2: Австралопітек
        Technology(
            id = "tech_bipedalism",
            name = "Постійне прямоходіння (Біпедалізм)",
            epoch = EpochType.AUSTRALOPITHECUS,
            category = TechCategory.BIOLOGY,
            costEvolution = 70,
            costMaterials = 25,
            iconEmoji = "👣",
            shortEffect = "+10 Їжі, +15 Моралі, вільні руки для праці",
            historicalDescription = "Вертикальна постава дозволила бачити хижаків у високій траві савани та переносити їжу і дитинчат на великі відстані.",
            foodBonus = 10,
            moraleBonus = 15
        ),
        Technology(
            id = "tech_pebble_anvil",
            name = "Камінь-ковадло для розбивання",
            epoch = EpochType.AUSTRALOPITHECUS,
            category = TechCategory.TOOLS,
            costEvolution = 85,
            costMaterials = 35,
            iconEmoji = "🪨",
            shortEffect = "+10 Матеріалів, доступ до кісткового мозку",
            historicalDescription = "Розбивання міцних горіхів і кісток важким кругляком на плоскому камені заклало основи моторної культури.",
            materialBonus = 10
        ),

        // Епоха 3: Людина прямоходяча (Homo erectus)
        Technology(
            id = "tech_fire_control",
            name = "Приборкання природного вогню",
            epoch = EpochType.HOMO_ERECTUS,
            category = TechCategory.SURVIVAL,
            costEvolution = 150,
            costMaterials = 60,
            iconEmoji = "🔥",
            shortEffect = "+30 Безпеки, +15 Їжі від термічної обробки",
            historicalDescription = "Підтримання багаття захищало від шаблезубих хижаків, зігрівало в холодні ночі та робило м'ясо легким для засвоєння.",
            warmthBonus = 30,
            foodBonus = 15
        ),
        Technology(
            id = "tech_acheulean_handaxe",
            name = "Ашельське рубило (Королево, Україна)",
            epoch = EpochType.HOMO_ERECTUS,
            category = TechCategory.TOOLS,
            costEvolution = 180,
            costMaterials = 70,
            iconEmoji = "🪓",
            shortEffect = "+20 Матеріалів, +10 Їжі від мисливців",
            historicalDescription = "Двобічно оброблене краплеподібне рубило — універсальний інструмент для рубання, здирання шкур і розбивання деревини.",
            materialBonus = 20,
            foodBonus = 10
        ),

        // Епоха 4: Неандерталець
        Technology(
            id = "tech_fur_clothing",
            name = "Одяг зі шкур та кам'яне проколювання",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            category = TechCategory.SURVIVAL,
            costEvolution = 250,
            costMaterials = 100,
            iconEmoji = "🧥",
            shortEffect = "+35 Захисту від холоду в Льодовиковий період",
            historicalDescription = "Очищені кам'яними скреблами шкури бізонів і мамонтів, зв'язані жилами, захищали від лютих морозів.",
            warmthBonus = 35
        ),
        Technology(
            id = "tech_mammoth_hut",
            name = "Каркасні житла з кісток мамонта",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            category = TechCategory.SURVIVAL,
            costEvolution = 280,
            costMaterials = 120,
            iconEmoji = "⛺",
            shortEffect = "+25 Безпеки, +8 до ліміту населення",
            historicalDescription = "Черепи, бивні та щелепи мамонтів утворювали міцний каркас купольних помешкань.",
            warmthBonus = 25,
            maxPopBonus = 8
        ),

        // Епоха 5: Людина розумна (Homo sapiens)
        Technology(
            id = "tech_spear_thrower",
            name = "Списокидалка та лук зі стрілами",
            epoch = EpochType.HOMO_SAPIENS,
            category = TechCategory.TOOLS,
            costEvolution = 350,
            costMaterials = 140,
            iconEmoji = "🏹",
            shortEffect = "+35 Їжі від мисливців, дистанційне полювання",
            historicalDescription = "Винайдення метальної зброї подвоїло дальність та ударну силу списа, мінімізуючи травми на полюванні.",
            foodBonus = 35
        ),
        Technology(
            id = "tech_meander_art",
            name = "Первісне мистецтво та музика (Мізин, Україна)",
            epoch = EpochType.HOMO_SAPIENS,
            category = TechCategory.SOCIETY_CULTURE,
            costEvolution = 380,
            costMaterials = 120,
            iconEmoji = "🎨",
            shortEffect = "+40 Моралі, символічне мислення та обряди",
            historicalDescription = "Браслети з бивня мамонта з меандровим орнаментом та перший ударний оркестр зі стоянки Мізин на Чернігівщині.",
            moraleBonus = 40
        )
    )

    fun getTechnologiesForEpoch(epoch: EpochType): List<Technology> {
        return allTechnologies.filter { it.epoch == epoch }
    }
}
