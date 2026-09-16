package com.example.data.model

enum class QuestType {
    BRACHIATION,    // Gathering / Agility minigame
    SAVANNAH_STEP,  // Exploration / Navigation minigame
    NUT_CRACKER,    // Tool crafting minigame
    FINAL_EXAM      // Final exam for the epoch
}

data class EpochQuest(
    val id: String,
    val type: QuestType,
    val epoch: EpochType,
    val title: String,
    val subtitle: String,
    val icon: String,
    val requiredEvolution: Int,
    val rewardEvolution: Int,
    val rewardFood: Int = 0,
    val rewardMaterials: Int = 0,
    val description: String,
    val historicalLesson: String
)

object EpochQuestCatalog {
    val quests: List<EpochQuest> = listOf(
        // Епоха 1: Дріопітек
        EpochQuest(
            id = "quest_brachiation_DRYOPITHECUS",
            epoch = EpochType.DRYOPITHECUS,
            type = QuestType.BRACHIATION,
            title = "1. Брахіація в кронах дерев",
            subtitle = "Збір плодів та розвиток гнучких рук",
            icon = "🌴",
            requiredEvolution = 0,
            rewardEvolution = 30,
            rewardFood = 25,
            description = "Дріопітек живе високо в кронах дерев міоценового пралісу. Допоможи йому перестрибувати з гілки на гілку способом брахіації (гойдання на руках) і збирати стиглі тропічні плоди!",
            historicalLesson = "Брахіація сформувала широкий плечовий пояс, рухливі зап'ястки та здатність тримати тулуб вертикально."
        ),
        EpochQuest(
            id = "quest_savannah_DRYOPITHECUS",
            epoch = EpochType.DRYOPITHECUS,
            type = QuestType.SAVANNAH_STEP,
            title = "2. Кліматична криза: спуск на землю",
            subtitle = "Перші кроки на двох лапах у високій траві",
            icon = "🌾",
            requiredEvolution = 25,
            rewardEvolution = 40,
            rewardFood = 15,
            description = "Клімат Землі 4 млн. років тому став сухішим. Ліси почали рідшати. Щоб дістатися до сусіднього гаю, треба подолати савану, озираючись на двох лапах для захисту від хижаків!",
            historicalLesson = "Щоб побачити хижаків у високій траві, предки людей були змушені підніматися на задні кінцівки. Так зароджувалося прямоходіння."
        ),
        EpochQuest(
            id = "quest_nut_cracker_DRYOPITHECUS",
            epoch = EpochType.DRYOPITHECUS,
            type = QuestType.NUT_CRACKER,
            title = "3. Природні знаряддя: Розколювання горіхів",
            subtitle = "Використання каменя як першого інструмента",
            icon = "🪨",
            requiredEvolution = 60,
            rewardEvolution = 45,
            rewardMaterials = 20,
            description = "Знайдено тверді лісові горіхи з поживною серединкою! Зубами їх не розкусити. Використай плоский камінь-ковадло та міцний важкий кругляк, щоб розбити шкарлупу!",
            historicalLesson = "Використання каміння та палиць без їхньої обробки — це перехідний етап від тваринної поведінки до усвідомленої праці."
        ),
        EpochQuest(
            id = "quest_final_exam_DRYOPITHECUS",
            epoch = EpochType.DRYOPITHECUS,
            type = QuestType.FINAL_EXAM,
            title = "4. Підсумковий іспит: Епоха Дріопітека",
            subtitle = "Перевірка знань для переходу до Австралопітека",
            icon = "🎓",
            requiredEvolution = 100,
            rewardEvolution = 50,
            description = "Дай відповіді на історичні запитання про епоху та особливості дріопітека, щоб розблокувати наступну епоху — Австралопітека!",
            historicalLesson = "Успішне складання іспиту про дріопітека відкриває наступний великий етап еволюції людини — Австралопітека."
        ),

        // Епоха 2: Австралопітек
        EpochQuest(
            id = "quest_brachiation_AUSTRALOPITHECUS",
            epoch = EpochType.AUSTRALOPITHECUS,
            type = QuestType.BRACHIATION,
            title = "1. Викопування бульб і корінців",
            subtitle = "Пошук їжі у посушливій савані",
            icon = "🍠",
            requiredEvolution = 100,
            rewardEvolution = 30,
            rewardFood = 25,
            description = "На відміну від предків у лісах, австралопітек живе у відкритій савані. Допоможи знайти поживні підземні бульби, коренеплоди та дикі злаки!",
            historicalLesson = "Перехід до живлення наземними рослинами та підземними частинами рослин розширив раціон австралопітеків."
        ),
        EpochQuest(
            id = "quest_savannah_AUSTRALOPITHECUS",
            epoch = EpochType.AUSTRALOPITHECUS,
            type = QuestType.SAVANNAH_STEP,
            title = "2. Прямоходіння та сканування горизонту",
            subtitle = "Впевнений біпедалізм у високій траві",
            icon = "👣",
            requiredEvolution = 130,
            rewardEvolution = 40,
            rewardFood = 15,
            description = "Пересуваючись саваною на двох ногах (біпедалізм), австралопітек отримує огляд поверх високих трав'янистих заростей для вчасного помічання хижаків.",
            historicalLesson = "Скелет 'Люсі' та інші знахідки доводять, що біпедалізм з'явився задовго до значного збільшення об'єму мозку."
        ),
        EpochQuest(
            id = "quest_nut_cracker_AUSTRALOPITHECUS",
            epoch = EpochType.AUSTRALOPITHECUS,
            type = QuestType.NUT_CRACKER,
            title = "3. Олдувайські чоппери: перші знаряддя",
            subtitle = "Сколювання гострих країв з гальки",
            icon = "🪓",
            requiredEvolution = 170,
            rewardEvolution = 45,
            rewardMaterials = 20,
            description = "Щоб розрізати товсту шкіру великих тварин або розколоти кістки, австралопітек навчився вдаряти каменем об камінь, створюючи примітивні гострі чоппери!",
            historicalLesson = "Олдувайська культура знарядь праці стала першою технологічною проміжною сходинкою в історії людства."
        ),
        EpochQuest(
            id = "quest_final_exam_AUSTRALOPITHECUS",
            epoch = EpochType.AUSTRALOPITHECUS,
            type = QuestType.FINAL_EXAM,
            title = "4. Підсумковий іспит: Епоха Австралопітека",
            subtitle = "Перевірка знань про біпедалізм та знаряддя",
            icon = "🎓",
            requiredEvolution = 250,
            rewardEvolution = 50,
            description = "Дай відповіді на історичні запитання про анатомію та поведінку австралопітека, щоб відкрити наступну епоху — Homo Erectus!",
            historicalLesson = "Успішне засвоєння етапу австралопітека відкриває шлях до появи людини прямоходячої."
        ),

        // Епоха 3: Homo Erectus
        EpochQuest(
            id = "quest_brachiation_HOMO_ERECTUS",
            epoch = EpochType.HOMO_ERECTUS,
            type = QuestType.BRACHIATION,
            title = "1. Колективне полювання",
            subtitle = "Здобування м'яса",
            icon = "🍖",
            requiredEvolution = 250,
            rewardEvolution = 40,
            rewardFood = 35,
            description = "Пітекантропи почали активно полювати групами, заганяючи тварин.",
            historicalLesson = "Більше м'яса в раціоні сприяло швидкому розвитку мозку."
        ),
        EpochQuest(
            id = "quest_savannah_HOMO_ERECTUS",
            epoch = EpochType.HOMO_ERECTUS,
            type = QuestType.SAVANNAH_STEP,
            title = "2. Велике розселення",
            subtitle = "Вихід з Африки",
            icon = "🌍",
            requiredEvolution = 300,
            rewardEvolution = 50,
            rewardFood = 20,
            description = "Прокладай шлях для свого племені через нові незвідані континенти — аж до стоянки Королево на Закарпатті.",
            historicalLesson = "Homo Erectus був першим видом гомінідів, який вийшов за межі Африки."
        ),
        EpochQuest(
            id = "quest_nut_cracker_HOMO_ERECTUS",
            epoch = EpochType.HOMO_ERECTUS,
            type = QuestType.NUT_CRACKER,
            title = "3. Приборкання вогню",
            subtitle = "Створення багаття",
            icon = "🔥",
            requiredEvolution = 350,
            rewardEvolution = 60,
            rewardMaterials = 30,
            description = "Знайди спосіб підтримувати вогонь. Це захистить плем'я від хижаків та холоду.",
            historicalLesson = "Приборкання вогню кардинально змінило життя предків, давши тепло і термічно оброблену їжу."
        ),
        EpochQuest(
            id = "quest_final_exam_HOMO_ERECTUS",
            epoch = EpochType.HOMO_ERECTUS,
            type = QuestType.FINAL_EXAM,
            title = "4. Підсумковий іспит: Homo Erectus",
            subtitle = "Перевірка знань для переходу до Неандертальця",
            icon = "🎓",
            requiredEvolution = 500,
            rewardEvolution = 70,
            description = "Доведи свої знання про пітекантропів та архантропів.",
            historicalLesson = "Володіння вогнем і ашельські рубила вивели людство на новий щабель."
        ),

        // Епоха 4: Неандерталець
        EpochQuest(
            id = "quest_brachiation_HOMO_NEANDERTHALENSIS",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            type = QuestType.BRACHIATION,
            title = "1. Полювання на мамонта",
            subtitle = "Виживання в льодовиковому періоді",
            icon = "🦣",
            requiredEvolution = 500,
            rewardEvolution = 50,
            rewardFood = 50,
            description = "Організуй небезпечне полювання на великого звіра в умовах холоду.",
            historicalLesson = "Неандертальці були майстерними мисливцями на велику дичину."
        ),
        EpochQuest(
            id = "quest_savannah_HOMO_NEANDERTHALENSIS",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            type = QuestType.SAVANNAH_STEP,
            title = "2. Пошук печер (Киїк-Коба)",
            subtitle = "Прихисток від заметілей",
            icon = "⛰️",
            requiredEvolution = 600,
            rewardEvolution = 60,
            rewardFood = 20,
            description = "Досліди крижані пустки в пошуках надійної печери, як-от Киїк-Коба в Криму.",
            historicalLesson = "Печери були природним захистом від суворих зим льодовикового періоду."
        ),
        EpochQuest(
            id = "quest_nut_cracker_HOMO_NEANDERTHALENSIS",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            type = QuestType.NUT_CRACKER,
            title = "3. Обробка шкур",
            subtitle = "Створення теплого одягу",
            icon = "🧥",
            requiredEvolution = 700,
            rewardEvolution = 60,
            rewardMaterials = 40,
            description = "Використай кам'яні скребла, щоб очистити шкури вбитих тварин і зшити з них одяг.",
            historicalLesson = "Теплий одяг дозволив вижити під час зледеніння Європи."
        ),
        EpochQuest(
            id = "quest_final_exam_HOMO_NEANDERTHALENSIS",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            type = QuestType.FINAL_EXAM,
            title = "4. Підсумковий іспит: Неандерталець",
            subtitle = "Перехід до Homo Sapiens",
            icon = "🎓",
            requiredEvolution = 850,
            rewardEvolution = 80,
            description = "Пройди тестування про неандертальців.",
            historicalLesson = "Неандертальці мали великий мозок, турбувалися про хворих та мали свої ритуали."
        ),

        // Епоха 5: Homo Sapiens
        EpochQuest(
            id = "quest_brachiation_HOMO_SAPIENS",
            epoch = EpochType.HOMO_SAPIENS,
            type = QuestType.BRACHIATION,
            title = "1. Заготівельна експедиція",
            subtitle = "Розширений збір ресурсів",
            icon = "🌾",
            requiredEvolution = 850,
            rewardEvolution = 60,
            rewardFood = 60,
            description = "Люди розумні стали набагато ефективніше використовувати ресурси свого середовища.",
            historicalLesson = "Комплексне використання рослин та тварин стало основою стабільності кроманьйонців."
        ),
        EpochQuest(
            id = "quest_savannah_HOMO_SAPIENS",
            epoch = EpochType.HOMO_SAPIENS,
            type = QuestType.SAVANNAH_STEP,
            title = "2. Заселення нових континентів",
            subtitle = "Міграції людей",
            icon = "🗺️",
            requiredEvolution = 950,
            rewardEvolution = 70,
            rewardFood = 25,
            description = "Долай льодовики, річки та океани, розселяючись по всій планеті.",
            historicalLesson = "Homo sapiens стали єдиним видом людей, який зміг заселити всі континенти, включаючи Америку і Австралію."
        ),
        EpochQuest(
            id = "quest_nut_cracker_HOMO_SAPIENS",
            epoch = EpochType.HOMO_SAPIENS,
            type = QuestType.NUT_CRACKER,
            title = "3. Мистецтво та Лук",
            subtitle = "Досконалі знаряддя Мізина",
            icon = "🏹",
            requiredEvolution = 1050,
            rewardEvolution = 80,
            rewardMaterials = 50,
            description = "Створи прикраси з меандровим орнаментом, виготов мікроліти та зроби лук зі стрілами.",
            historicalLesson = "Символічне мислення, мистецтво і складена зброя — найвищі досягнення палеоліту."
        ),
        EpochQuest(
            id = "quest_final_exam_HOMO_SAPIENS",
            epoch = EpochType.HOMO_SAPIENS,
            type = QuestType.FINAL_EXAM,
            title = "4. Підсумковий іспит: Homo Sapiens",
            subtitle = "Фінал еволюційної подорожі",
            icon = "🎓",
            requiredEvolution = 1200,
            rewardEvolution = 100,
            description = "Доведи, що ти опанував всю історію еволюції людини!",
            historicalLesson = "Людина розумна — вершина еволюції, що заклала основи для переходу до цивілізації."
        )
    )

    fun getQuestsForEpoch(epoch: EpochType): List<EpochQuest> {
        return quests.filter { it.epoch == epoch }
    }
}
