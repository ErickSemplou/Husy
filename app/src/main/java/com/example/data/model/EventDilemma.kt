package com.example.data.model

data class DilemmaChoice(
    val title: String,
    val description: String,
    val deltaFood: Int = 0,
    val deltaMaterials: Int = 0,
    val deltaPopulation: Int = 0,
    val deltaEvolution: Int = 0,
    val deltaWarmth: Int = 0,
    val deltaMorale: Int = 0,
    val historicalOutcome: String
)

data class EventDilemma(
    val id: String,
    val epoch: EpochType,
    val title: String,
    val situation: String,
    val historicalContext: String,
    val choices: List<DilemmaChoice>
)

object DilemmaCatalog {
    val allDilemmas: List<EventDilemma> = listOf(
        // Дріопітек
        EventDilemma(
            id = "dil_dryo_drought",
            epoch = EpochType.DRYOPITHECUS,
            title = "Посуха в кронах міоценового пралісу",
            situation = "Джерела в лісі пересихають, соковитих плодів на деревах стає все менше. На горизонті видно зелені галявини савани, але там висока суха трава і можуть ховатися шаблезубі хижаки.",
            historicalContext = "Зміна клімату 4 млн р.т. змусила предків людини залишати дерева та пристосовуватися до життя на відкритих просторах.",
            choices = listOf(
                DilemmaChoice(
                    title = "Спуститися до савани на двох кінцівках",
                    description = "Ризикнути вийти у високу траву, озираючись на всі боки.",
                    deltaEvolution = 25,
                    deltaFood = 20,
                    deltaMorale = 10,
                    deltaWarmth = -5,
                    historicalOutcome = "Зграя знайшла нові джерела плодів і навчилася оглядати савану на двох лапах!"
                ),
                DilemmaChoice(
                    title = "Залишитися в кронах і шукати залишки коріння",
                    description = "Безпечно, але їжі дуже мало.",
                    deltaFood = 5,
                    deltaWarmth = 10,
                    deltaMorale = -10,
                    historicalOutcome = "Зграя перечекала посуху в безпеці, але втратила шанс на розвиток нових навичок."
                )
            )
        ),
        EventDilemma(
            id = "dil_dryo_nuts",
            epoch = EpochType.DRYOPITHECUS,
            title = "Знахідка твердих тропічних горіхів",
            situation = "Під старим баобабом знайдено велику купу поживних горіхів, але їхня шкаралупа занадто міцна для зубів.",
            historicalContext = "Використання каміння для розбивання горіхів — один із перших прикладів інструментальної поведінки предків людини.",
            choices = listOf(
                DilemmaChoice(
                    title = "Взяти важкий річковий кругляк і камінь-ковадло",
                    description = "Покласти горіх на плаский камінь і вдарити.",
                    deltaEvolution = 30,
                    deltaMaterials = 15,
                    deltaFood = 25,
                    historicalOutcome = "Смачні горіхи врятували плем'я від голоду, а руки запам'ятали першу працю з каменем!"
                ),
                DilemmaChoice(
                    title = "Намагатися розгризти зубами",
                    description = "Швидко, але можна пошкодити щелепу.",
                    deltaFood = 8,
                    deltaMorale = -10,
                    historicalOutcome = "Деякі горіхи вдалося з'їсти, але кілька родичів пошкодили зуби."
                )
            )
        ),

        // Австралопітек
        EventDilemma(
            id = "dil_austra_predator",
            epoch = EpochType.AUSTRALOPITHECUS,
            title = "Поява динофенікса у високій траві",
            situation = "Під час збирання насіння зграя помітила небезпечного хижака. Треба швидко попередити всіх.",
            historicalContext = "Колективна взаємодія та огляд території на двох ногах рятували життя раннім австралопітекам.",
            choices = listOf(
                DilemmaChoice(
                    title = "Піднятися на дві ноги та голосно кричати й стукати палицями",
                    description = "Злякати хижака шумом і єдністю групи.",
                    deltaEvolution = 20,
                    deltaMorale = 25,
                    deltaWarmth = 10,
                    historicalOutcome = "Хижак злякався організованої зграї та відступив!"
                ),
                DilemmaChoice(
                    title = "Швидко тікати до найближчих дерев",
                    description = "Кожен рятується самостійно.",
                    deltaFood = -10,
                    deltaMorale = -15,
                    historicalOutcome = "Усі врятувалися на деревах, але згубили зібрану їжу."
                )
            )
        ),

        // Homo erectus
        EventDilemma(
            id = "dil_erectus_fire",
            epoch = EpochType.HOMO_ERECTUS,
            title = "Блискавка вдарила в сухе дерево",
            situation = "Після бурі дерево зайнялося вогнем. Плем'я дивиться на палаюче полум'я зі страхом і захопленням.",
            historicalContext = "Homo erectus навчився брати палаючі гілки від природних пожеж і підтримувати багаття.",
            choices = listOf(
                DilemmaChoice(
                    title = "Взяти палаючу гілку та підтримувати багаття в таборі",
                    description = "Подолати страх перед вогнем.",
                    deltaEvolution = 50,
                    deltaWarmth = 40,
                    deltaFood = 20,
                    deltaMorale = 30,
                    historicalOutcome = "Вогонь став серцем табору! Він зігріває ночами та захищає від усіх звірів."
                ),
                DilemmaChoice(
                    title = "Загасити водою зі страху",
                    description = "Уникнути ризику обпектися.",
                    deltaMorale = -10,
                    historicalOutcome = "Табір залишився в темряві й холоді."
                )
            )
        ),

        // Неандерталець
        EventDilemma(
            id = "dil_neander_blizzard",
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            title = "Крижаний буран Льодовикового періоду",
            situation = "Температура різко впала. Потрібно вирішити, як зберегти тепло племені в печері.",
            historicalContext = "Зшивання шкур і підтримання вогнища в центрі печери рятувало неандертальців від вимерзання.",
            choices = listOf(
                DilemmaChoice(
                    title = "Зшити подвійні шкури бізонів жилами та утеплити вхід",
                    description = "Використати кістяні голки та скребла.",
                    deltaEvolution = 35,
                    deltaMaterials = -20,
                    deltaWarmth = 50,
                    deltaMorale = 20,
                    historicalOutcome = "Печера стала затишною і теплою, буран пережито без жодної втрати!"
                )
            )
        ),

        // Homo sapiens
        EventDilemma(
            id = "dil_sapiens_art",
            epoch = EpochType.HOMO_SAPIENS,
            title = "Обряд біля вогнища: створення першого малюнка",
            situation = "Мисливці повернулися після вдалого полювання на мамонта. Молодий майстер хоче увічнити цей день вохрою на стіні.",
            historicalContext = "Народження первісного мистецтва та обрядів у верхньому палеоліті (Мізинська культура).",
            choices = listOf(
                DilemmaChoice(
                    title = "Підтримати майстра та зіграти на музичних кістках мамонта",
                    description = "Об'єднати плем'я мистецтвом і музикою.",
                    deltaEvolution = 45,
                    deltaMorale = 50,
                    historicalOutcome = "Первісна пісня та малюнок згуртували плем'я, народивши культуру!"
                )
            )
        )
    )

    fun getDilemmasForEpoch(epoch: EpochType): List<EventDilemma> {
        return allDilemmas.filter { it.epoch == epoch }
    }

    fun getRandomDilemmaForEpoch(epoch: EpochType): EventDilemma? {
        val candidates = getDilemmasForEpoch(epoch)
        return if (candidates.isNotEmpty()) candidates.random() else allDilemmas.firstOrNull()
    }
}
