package com.example.data.model

data class QuizQuestion(
    val id: String,
    val epoch: EpochType,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val scientificExplanation: String
)

data class EpochQuiz(
    val epoch: EpochType,
    val title: String,
    val passingScore: Int = 3,
    val questions: List<QuizQuestion>,
    val rewardEvolutionPoints: Int = 80
)

object QuizCatalog {
    val epochQuizzes: Map<EpochType, EpochQuiz> = mapOf(
        EpochType.DRYOPITHECUS to EpochQuiz(
            epoch = EpochType.DRYOPITHECUS,
            title = "Історичний іспит: Дріопітек та перші предки",
            rewardEvolutionPoints = 60,
            questions = listOf(
                QuizQuestion(
                    id = "q_dryo_1",
                    epoch = EpochType.DRYOPITHECUS,
                    question = "Коли приблизно жив дріопітек в історії антропогенезу?",
                    options = listOf(
                        "Близько 4 млн. років тому (первісна доба)",
                        "100 років тому",
                        "У часи Середньовіччя",
                        "200 тисяч років тому"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Дріопітек жив близько 4 млн. років тому, коли теплі вологі тропічні ліси почали поступатися місцем рідколіссям та саванам."
                ),
                QuizQuestion(
                    id = "q_dryo_2",
                    epoch = EpochType.DRYOPITHECUS,
                    question = "Де переважно мешкав дріопітек і як пересувався?",
                    options = listOf(
                        "У кронах дерев, пересуваючись на руках (брахіація)",
                        "У підводних печерах",
                        "У крижаних юртах",
                        "На піщаних пляжах"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Слово 'Дріопітек' перекладається як 'деревна мавпа'. Він жив на деревах і гойдався на гілках завдяки міцним та гнучким рукам."
                ),
                QuizQuestion(
                    id = "q_dryo_3",
                    epoch = EpochType.DRYOPITHECUS,
                    question = "Що змусило предків людини спускатися на землю і підніматися на дві лапи?",
                    options = listOf(
                        "Похолодання та висихання клімату, що перетворило ліси на відкриту савану",
                        "Втома від лазіння по деревах",
                        "Шукали зручніше місце поспати",
                        "Політ на іншу планету"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Зміна клімату скоротила площу пралісів. Щоб долати відкриті трав'янисті простори і бачити хижаків, предкам довелося стати на дві лапи."
                ),
                QuizQuestion(
                    id = "q_dryo_4",
                    epoch = EpochType.DRYOPITHECUS,
                    question = "Які перші предмети природи використовував дріопітек?",
                    options = listOf(
                        "Необроблені палиці та камені для розбивання горіхів",
                        "Залізні мечі",
                        "Глиняний розписний посуд",
                        "Мобільні телефони"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Дріопітек ще не вмів виготовляти штучні знаряддя, але вже брав природні важкі камені та палиці."
                )
            )
        ),

        EpochType.AUSTRALOPITHECUS to EpochQuiz(
            epoch = EpochType.AUSTRALOPITHECUS,
            title = "Історичний іспит: Австралопітек (Люсі)",
            rewardEvolutionPoints = 80,
            questions = listOf(
                QuizQuestion(
                    id = "q_austra_1",
                    epoch = EpochType.AUSTRALOPITHECUS,
                    question = "Як перекладається назва 'Австралопітек'?",
                    options = listOf("Південна мавпа", "Північна людина", "Водяний мешканець", "Печерний мисливець"),
                    correctIndex = 0,
                    scientificExplanation = "Назва походить від латинського 'australis' (південний) та грецького 'pithekos' (мавпа)."
                ),
                QuizQuestion(
                    id = "q_austra_2",
                    epoch = EpochType.AUSTRALOPITHECUS,
                    question = "Яка головна еволюційна перемога австралопітека (зокрема Люсі)?",
                    options = listOf(
                        "Впевнене постійне прямоходіння на двох ногах (біпедалізм)",
                        "Будівництво кам'яних фортець",
                        "Винайдення письма",
                        "Створення металевої зброї"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Австралопітек остаточно звільнив передні кінцівки (руки) завдяки вертикальній ході на двох ногах."
                ),
                QuizQuestion(
                    id = "q_austra_3",
                    epoch = EpochType.AUSTRALOPITHECUS,
                    question = "Яку назву має перша найдавніша культура обробки каменя, пов'язана з австралопітеками?",
                    options = listOf(
                        "Олдувайська культура (чоппери)",
                        "Ашельські рубила",
                        "Трипільська кераміка",
                        "Залізна доба"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Олдувайські чоппери — це примітивні гальки із кількома сколами для розколювання кісток та розрізання м'яса."
                ),
                QuizQuestion(
                    id = "q_austra_4",
                    epoch = EpochType.AUSTRALOPITHECUS,
                    question = "Яку перевагу надавало прямоходіння австралопітекам у відкритій савані?",
                    options = listOf(
                        "Огляд горизонту поверх високої трави та вільні руки для переношення їжі",
                        "Можливість літати над саваною",
                        "Повна відсутність потреби в їжі",
                        "Захист від суворих снігопадів"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Біпедалізм дозволяв помічати хижаків здалеку, зменшував перегрів тіла від сонця та звільнив руки."
                )
            )
        ),

        EpochType.HOMO_ERECTUS to EpochQuiz(
            epoch = EpochType.HOMO_ERECTUS,
            title = "Історичний іспит: Людина прямоходяча",
            rewardEvolutionPoints = 100,
            questions = listOf(
                QuizQuestion(
                    id = "q_erectus_1",
                    epoch = EpochType.HOMO_ERECTUS,
                    question = "Яка найдавніша стоянка первісної людини в Україні та Європі (1.4 млн р.т.)?",
                    options = listOf("Стоянка Королево на Закарпатті", "Київський замок", "Одеса", "Львівська ратуша"),
                    correctIndex = 0,
                    scientificExplanation = "Стоянка біля смт Королево на Закарпатті доводить проживання Homo erectus понад 1.4 млн років тому."
                ),
                QuizQuestion(
                    id = "q_erectus_2",
                    epoch = EpochType.HOMO_ERECTUS,
                    question = "Яке фундаментальне відкриття зробила Людина прямоходяча?",
                    options = listOf("Приборкання вогню та рубило", "Винайдення колеса", "Електрика", "Пароплав"),
                    correctIndex = 0,
                    scientificExplanation = "Вогонь дозволив зігріватися, готувати їжу та захищатися від диких звірів."
                ),
                QuizQuestion(
                    id = "q_erectus_3",
                    epoch = EpochType.HOMO_ERECTUS,
                    question = "Як Homo erectus розширив географію проживання предків людини?",
                    options = listOf(
                        "Першим з предків розселився за межі Африки в Євразію",
                        "Оселився на дні океану",
                        "Перелетів на інший континент",
                        "Жив виключно в антарктичних льодах"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Homo erectus здійснив перший великий вихід з Африки, розселившись по всій Євразії."
                ),
                QuizQuestion(
                    id = "q_erectus_4",
                    epoch = EpochType.HOMO_ERECTUS,
                    question = "Що сприяло стрімкому збільшенню об'єму мозку у Homo erectus?",
                    options = listOf(
                        "Споживання термічно обробленої м'ясної їжі на вогні",
                        "Харчування лише сухою корою дерев",
                        "Використання мобільних пристроїв",
                        "Списування іспитів"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Приготування їжі на вогні покращило засвоєння білків та поживних речовин для розвитку мозку."
                )
            )
        ),

        EpochType.HOMO_NEANDERTHALENSIS to EpochQuiz(
            epoch = EpochType.HOMO_NEANDERTHALENSIS,
            title = "Історичний іспит: Неандертальці",
            rewardEvolutionPoints = 120,
            questions = listOf(
                QuizQuestion(
                    id = "q_neand_1",
                    epoch = EpochType.HOMO_NEANDERTHALENSIS,
                    question = "У яких складних кліматичних умовах жили неандертальці?",
                    options = listOf("У суворий Льодовиковий період", "У пустелі Сахара", "У тропічних джунглях", "На океанських островах"),
                    correctIndex = 0,
                    scientificExplanation = "Неандертальці пристосувалися до крижаного клімату, шили одяг зі шкур і жили в печерах."
                ),
                QuizQuestion(
                    id = "q_neand_2",
                    epoch = EpochType.HOMO_NEANDERTHALENSIS,
                    question = "Які соціокультурні ритуали вперше виявили у неандертальців?",
                    options = listOf(
                        "Перші поховання померлих та піклування про поранених",
                        "Заснування банківської системи",
                        "Виробництво фарфорових ваз",
                        "Будівництво залізниці"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Знахідки печер з квітами у похованнях доводять зародження турботи про ближніх і вірувань."
                ),
                QuizQuestion(
                    id = "q_neand_3",
                    epoch = EpochType.HOMO_NEANDERTHALENSIS,
                    question = "Яке знаряддя праці вдосконалили неандертальці (мустьєрська культура)?",
                    options = listOf("Гостроконечники та кремінні скребла для шкур", "Бронзові гармати", "Залізні мечi", "Арбалети"),
                    correctIndex = 0,
                    scientificExplanation = "Мустьєрська культура відзначається виготовленням досконалих скребел та составних списів."
                ),
                QuizQuestion(
                    id = "q_neand_4",
                    epoch = EpochType.HOMO_NEANDERTHALENSIS,
                    question = "Яка відома неандертальська стоянка знаходиться в Україні у Криму?",
                    options = listOf("Грот Киїк-Коба", "Хортиця", "Софіївка", "Асканія-Нова"),
                    correctIndex = 0,
                    scientificExplanation = "У печері Киїк-Коба виявлено рештки дорослого неандертальця та немовляти."
                )
            )
        ),

        EpochType.HOMO_SAPIENS to EpochQuiz(
            epoch = EpochType.HOMO_SAPIENS,
            title = "Історичний іспит: Людина розумна (Кроманьйонець)",
            rewardEvolutionPoints = 150,
            questions = listOf(
                QuizQuestion(
                    id = "q_sapiens_1",
                    epoch = EpochType.HOMO_SAPIENS,
                    question = "Чим відома кроманьйонська стоянка Мізин на Чернігівщині?",
                    options = listOf(
                        "Музичними інструментами з кісток мамонта та орнаментом-меандром",
                        "Першим залізничним вокзалом",
                        "Видобутком нафти",
                        "Кам'яними пірамідами"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Мізин дав світові зразки первісного мистецтва, меандровий браслет та ударні музичні інструменти."
                ),
                QuizQuestion(
                    id = "q_sapiens_2",
                    epoch = EpochType.HOMO_SAPIENS,
                    question = "Яка риса мислення Homo Sapiens дозволила їм об'єднувати великі племена?",
                    options = listOf(
                        "Розвинене абстрактне мислення, наочне мистецтво та мова",
                        "Читання думок на відстані",
                        "Використання супутникового зв'язку",
                        "Наявність залізних шоломів"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Розвинена членороздільна мова та абстрактне мислення дозволили ділитися досвідом та гуртуватися."
                ),
                QuizQuestion(
                    id = "q_sapiens_3",
                    epoch = EpochType.HOMO_SAPIENS,
                    question = "Яке метальне знаряддя винайшли кроманьйонці для полювання на великих звірів?",
                    options = listOf("Спис із кістяним наконечником та списометалочку", "Чавунну гармату", "Лазерний пульт", "Залізну булаву"),
                    correctIndex = 0,
                    scientificExplanation = "Списометалочка суттєво збільшила дальність та силу кидка списа."
                ),
                QuizQuestion(
                    id = "q_sapiens_4",
                    epoch = EpochType.HOMO_SAPIENS,
                    question = "Що символізує винайдення печерного живопису (наприклад, печера Альтаміра)?",
                    options = listOf(
                        "Виникнення справжнього образотворчого мистецтва та духовних уявлень",
                        "Спроба продати печеру",
                        "Будівельне маркування",
                        "Карти доріг"
                    ),
                    correctIndex = 0,
                    scientificExplanation = "Печерні малюнки тварин відображають духовний світ і магічні мисливські ритуали Homo Sapiens."
                )
            )
        )
    )

    fun getQuizForEpoch(epoch: EpochType): EpochQuiz {
        return epochQuizzes[epoch] ?: epochQuizzes[EpochType.DRYOPITHECUS]!!
    }
}
