package com.example.lab_2_3

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reg.*

//Обьявление дата класов для таблиц БД
@Entity
data class UsersTable(
    @ColumnInfo var name: String?,
    @ColumnInfo var surname: String?,
    @ColumnInfo var pat: String?,
    @ColumnInfo var login: String?,
    @ColumnInfo var pass: String?,
    @ColumnInfo var role: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class TestTable(
    @ColumnInfo var ques: String?,
    @ColumnInfo var var1: String?,
    @ColumnInfo var var2: String?,
    @ColumnInfo var var3: String?,
    @ColumnInfo var approve: String?,
    @ColumnInfo var kit: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class ResultTable(
    @ColumnInfo var login: String?,
    @ColumnInfo var result: String?,
    @ColumnInfo var name: String?,
    @ColumnInfo var surname: String?,
    @ColumnInfo var pat: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class AnswerTable(
    @ColumnInfo var login: String?,
    @ColumnInfo var ques: String?,
    @ColumnInfo var answ: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Dao
interface ReadoutModelDao {
    // методы для юзеров
    @get:Query("select * from UsersTable")            // "get:" означает применение аннотации "Query" к геттеру (функцию геттера для переменной allReadoutItems вручную не пишем)
    val allReadoutItems: List<UsersTable>             // Обёртываем возвращаемое значение LiveData<...> чтобы отслеживать изменения в базе. При изменении данных будут рассылаться уведомления

    @Query("select * from UsersTable where id = :id")
    fun getReadoutById(id: Long): UsersTable

    @Query("select * from UsersTable where login = :login")
    fun getReadoutByLogin(login: String): List<UsersTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(readoutModel: UsersTable)

    @Update
    fun updateReadout(readoutModel: UsersTable)

    @Delete
    fun deleteReadout(readoutModel: UsersTable)

    //методы для теста
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTest(readoutModel: TestTable)

    @get:Query("select * from TestTable")
    val getallTest: List<TestTable>

    @Query("select * from TestTable where kit = :kit")
    fun getTestByKit(kit: String): List<TestTable>

    @Query("select * from TestTable where id = :id")
    fun getTestById(id: Long): TestTable

    @Query("DELETE FROM TestTable")
    fun cleantable()

    //Методы для Результатов
    @Query("select * from ResultTable where login = :login")
    fun getResultByLogin(login: String): List<ResultTable>

    @get:Query("select * from ResultTable")
    val getallResult: List<ResultTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addResult(readoutModel: ResultTable)

    //Методы для таблицы Ответов
    @Query("select * from AnswerTable where login = :login")
    fun getAnswerByLogin(login: String): List<AnswerTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAnswer(readoutModel: AnswerTable)
}

/*
 * Описание базы данных.
 */
@Database(entities = [UsersTable::class, TestTable::class, ResultTable::class, AnswerTable::class], version = 1)
abstract class TestDatabase : RoomDatabase() {

    abstract fun readoutDAO(): ReadoutModelDao           // Описываем абстрактные методы для получения объектов интерфейса BorrowModelDao

    companion object {
        var INSTANCE: TestDatabase? = null

        fun getAppDataBase(context: Context): TestDatabase? {


            if (INSTANCE == null) {
                synchronized(TestDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, TestDatabase::class.java, "TestDB")
                        .fallbackToDestructiveMigration()
                        .addCallback(onCreateCallBack)
                        .build()
                }
            }
            return INSTANCE
        }

        //Записываем в БД при создании наш список вопросов теста
        var onCreateCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Observable.fromCallable {

                    val base = TestDatabase.getAppDataBase(MyApp.appContext)

                    if (base != null) {
                        val myDao = base.readoutDAO()
                        //set 1
                        myDao.addTest(
                            TestTable(
                                "Какую первую программу обычно пишут программисты?",
                                "Для взлома аккаунта «ВКонтакте». Такая программа есть у каждого программиста",
                                "Hello world",
                                "Сортировку «пузырьком»",
                                "Hello world",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Представим гипотетическую ситуацию, в которой программа скомпилировалась с первого раза. Как вы поступите?",
                                "Выключу комп и спокойно пойду спать — дело сделано",
                                "Порадуюсь за себя и продолжу писать код",
                                "Буду искать ошибку в компиляторе, где-то она должна быть",
                                "Буду искать ошибку в компиляторе, где-то она должна быть",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Допустим, вы пишете проект, и заказчик утвердил документ, в котором чётко написано, что он хочет получить в результате. Назовём его ТЗ. Изменятся ли требования в процессе работы над проектом?",
                                "Изменятся, конечно",
                                "Не изменятся. Вы же сами сказали, что всё чётко зафиксировано",
                                "Ну немножко",
                                "Изменятся, конечно",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Какой правильный ответ на вопрос про рекурсию?",
                                "Да",
                                "42",
                                "Какой правильный ответ на вопрос про рекурсию?",
                                "Какой правильный ответ на вопрос про рекурсию?",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Представьте, что вы пишете программу и при попытке её сборки компилятор выдал вам одну ошибку. Вы исправили её и пробуете собрать проект ещё раз. Сколько теперь будет ошибок?",
                                "Была одна, теперь ошибок не будет",
                                "Неизвестно",
                                "Ещё одна ошибка",
                                "Неизвестно",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Вы пришли на проект, над которым раньше работал другой программист. Что можно сказать о его коде?",
                                "Его код просто ужасен, ну кто так пишет!",
                                "Надо сначала детально изучить проект, чтобы понять это",
                                "В его коде есть чему поучиться",
                                "Его код просто ужасен, ну кто так пишет!",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Перед вами три дерева. На том, что посередине, сидит кот. На дереве с каким индексом сидит кот?",
                                "2",
                                "1",
                                "0",
                                "1",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Теперь чуть сложнее. Что такое Пик Балмера?",
                                "Гора в Северной Америке",
                                "Феномен о том, что при определённой концентрации алкоголя в крови программистские способности резко возрастают",
                                "Яхта Стива Балмера — бывшего генерального директора Microsoft",
                                "Феномен о том, что при определённой концентрации алкоголя в крови программистские способности резко возрастают",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что такое стринги?",
                                "Разновидность мини-трусиков",
                                "«Верёвки» на английском",
                                "Несколько переменных типа «строка»",
                                "Несколько переменных типа «строка»",
                                "0"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Какое максимальное число из перечисленных можно показать пальцами одной руки?",
                                "5",
                                "31",
                                "32",
                                "31",
                                "0"
                            )
                        )
                        //set 2
                        myDao.addTest(
                            TestTable(
                                "Древнерусское государство образовалось на территории:",
                                "Междуречья Рейна и Одера",
                                "Балканского полуострова",
                                "Восточноевропейской равнины",
                                "Восточноевропейской равнины",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Какое божество в языческом пантеоне восточных славян отвечало за плодородие?",
                                "Велес",
                                "Ярило",
                                "Перун",
                                "Ярило",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Гостомысл – это:",
                                "Князь, крестивший Русь",
                                "Варяжский наемник",
                                "Легендарный предводитель славян и предок Рюрика",
                                "Легендарный предводитель славян и предок Рюрика",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Эпоха первых правителей Руси характеризовалась:",
                                "Мощными центробежными и сепаратистскими тенденциями",
                                "Становлением единоличной власти князя",
                                "Отходом от языческих верований",
                                "Мощными центробежными и сепаратистскими тенденциями",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Первая русская летопись называлась:",
                                "Ипатьевская летопись",
                                "Новгородская первая летопись",
                                "Повесть временных лет",
                                "Повесть временных лет",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Первый письменный свод законов Древней Руси назывался:",
                                "ПСЗРИ",
                                "Духовные грамоты",
                                "Правда Ярослава",
                                "Правда Ярослава",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Съезд в Любече закрепил:",
                                "Окончательное закрепление феодальных порядков на территории Древнерусского государства",
                                "Тенденции к консолидации представителей рода Рюриковичей",
                                "Теологический характер государства",
                                "Окончательное закрепление феодальных порядков на территории Древнерусского государства",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Первым московским князем был:",
                                "Александр Ярославич",
                                "Даниил Александрович",
                                "Юрий Владимирович",
                                "Даниил Александрович",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Битва на Калке состоялась в:",
                                "1066 г.",
                                "1223 г.",
                                "1242 г.",
                                "1223 г.",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Оборона Рязани состоялась в:",
                                "1237 г.",
                                "1380 г.",
                                "1242 г.",
                                "1237 г.",
                                "1"
                            )
                        )
                      //set 3
                        myDao.addTest(
                            TestTable(
                                "Назовите автора произведений Последний поклон, Царь- рыба.",
                                "В.Белов",
                                "В.Тендряков",
                                "В.Астафьев",
                                "В.Астафьев",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Назовите автора статей: Что такое обломовщина?, Когда же придёт настоящий день?, Луч света в тёмном царстве.",
                                "Н.А.Добролюбов",
                                "Л.И.Писарев",
                                "В.Г.Белинский",
                                "Н.А.Добролюбов",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Укажите, какие произведения принадлежат перу А.Солженицына",
                                "В окопах Сталинграда, Крик",
                                "Огниво, Точка кипения",
                                "Матрёнин двор, В круге первом",
                                "Матрёнин двор, В круге первом",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Кому из критиков принадлежат статьи о романе А.С.Пушкина Евгений Онегин?",
                                "Д.И.Писареву",
                                "В.Г.Белинскому",
                                "Н.А.Добролюбову",
                                "В.Г.Белинскому",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "В каком произведении М.Булгакова слышится предупреждение о том, что нарушение естественного хода вещей приводит к необратимым последствиям?",
                                "Роковые яйца",
                                "Собачье сердце",
                                "Белая гвардия",
                                "Собачье сердце",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Укажите писателя, перу которого принадлежит повесть Обелиск",
                                "В.Быков",
                                "В.Гроссман",
                                "Ю.Бондарев",
                                "В.Быков",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Назовите литературное течение, возникшее в русской литературе, начала XX века, которому было свойственно... приятие земного мира в его зримой конкретности. Острый взгляд на подробности бытия, живое и непосредственное ощущение природы, культуры, мироздания и вещного мира",
                                "Акмеизм",
                                "Футуризм",
                                "Символизм",
                                "Акмеизм",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Кто из действующих лиц комедии А.С.Грибоедова Горе от ума произносит следующие слова: Хотел объехать целый свет и не объехал сотой доли?",
                                "Чацкий",
                                "Скалозуб",
                                "Молчалин",
                                "Чацкий",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Кто из героев романа М.Ю.Лермонтова был ... такой тоненький, беленький, на нем мундир был такой новенький, что я тотчас догадался, что он на Кавказе недавно?",
                                "Печорин.",
                                "Максим Максимыч",
                                "Грушницкий",
                                "Печорин.",
                                "2"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Назовите автора трагедии Ариадна.",
                                "А.Ахматова",
                                "К.Бальмонт",
                                "М.Цветаева",
                                "М.Цветаева",
                                "2"
                            )
                        )
                        //set 4
                        myDao.addTest(
                            TestTable(
                                "Из каких основных частей состоит автомобиль",
                                "Двигатель, кузов, шасси",
                                "Двигатель, трансмиссия, кузов",
                                "Двигатель, шасси, рама",
                                "Двигатель, кузов, шасси",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Как расшифровывается ВАЗ 21011",
                                "Волынский автозавод, объем двигателя 1.8л, седан, 11 модель",
                                "Волжский автомобильный завод, легковой, объем двигателя до 1.8л, 11 модель",
                                "Волжский автомобильный завод, фургон, объем двигателя 1.4л, 11 модель",
                                "Волжский автомобильный завод, легковой, объем двигателя до 1.8л, 11 модель",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Виды двигателей внутреннего сгорания в зависимости от типа топлива",
                                "Бензин, дизельное топливо, газ",
                                "Бензин, сжиженный газ, дизельное топливо",
                                "Жидкое, газообразное, комбинированное",
                                "Жидкое, газообразное, комбинированное",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Перечислите основные детали ДВС",
                                "Коленчатый вал, задний мост, поршень, блок цилиндров",
                                "Шатун, коленчатый вал, поршень, цилиндр",
                                "Трансмиссия, поршень, головка блока, распределительный вал",
                                "Шатун, коленчатый вал, поршень, цилиндр",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что называется рабочим объемом цилиндра",
                                "Объем цилиндра освобождаемый поршнем при движении от ВМТ к НМТ",
                                "Объем цилиндра над поршнем в ВМТ",
                                "Объем цилиндра над поршнем в НМТ",
                                "Объем цилиндра освобождаемый поршнем при движении от ВМТ к НМТ",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что называется литражом двигателя",
                                "Сумма полных объемов всех цилиндров двигателя",
                                "Сумма рабочих объемов всех цилиндров двигателя",
                                "Сумма объемов камер сгорания всех цилиндров двигателя",
                                "Сумма рабочих объемов всех цилиндров двигателя",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что показывает степень сжатия",
                                "Отношение объема камеры сгорания к полному объему цилиндра",
                                "Разницу между рабочим и полным объемом цилиндра",
                                "Во сколько раз полный объем больше объема камеры сгорания",
                                "Во сколько раз полный объем больше объема камеры сгорания",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что поступает в цилиндр карбюраторного двигателя при такте «впуск»",
                                "Сжатый, очищенный воздух",
                                "Смесь дизельного топлива и воздуха",
                                "Смесь бензина и воздуха",
                                "Смесь бензина и воздуха",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "За счет чего воспламеняется горючая смесь в дизельном двигателе",
                                "За счет форсунки",
                                "За счет самовоспламенения",
                                "С помощью искры которая образуется на свече",
                                "За счет самовоспламенения",
                                "3"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "В какой последовательности происходят такты в 4-х тактном ДВС",
                                "Выпуск, рабочий ход, сжатие, впуск",
                                "Выпуск, сжатие, рабочий ход, впуск",
                                "Впуск, сжатие, рабочий ход, выпуск",
                                "Впуск, сжатие, рабочий ход, выпуск",
                                "3"
                            )
                        )
                    }


                }.subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}
