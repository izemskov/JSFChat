package ru.develgame.jsfgame

import ru.develgame.jsfgame.domain.Person
import ru.develgame.jsfgame.jms.PersonsChangeInformer
import javax.ejb.ConcurrencyManagement
import javax.ejb.ConcurrencyManagementType
import javax.ejb.Lock
import javax.ejb.LockType.READ
import javax.ejb.LockType.WRITE
import javax.ejb.Singleton
import javax.inject.Inject

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
open class PersonsRegistry {
    @Inject
    private lateinit var personsChangeInformer: PersonsChangeInformer

    @Inject
    private lateinit var chatBean: ChatBean

    open val persons: MutableMap<String, Person> = mutableMapOf()

    @Lock(WRITE)
    open fun addPerson(person: Person) {
        persons[person.uuid] = person

        personsChangeInformer.sendMessage()

        chatBean.personJoined(person)
    }

    @Lock(WRITE)
    open fun removePerson(person: Person) {
        persons.remove(person.uuid)

        personsChangeInformer.sendMessage()
    }

    @Lock(READ)
    open fun getOtherPersonsList(uuid: String): MutableList<Person> {
        val res: MutableList<Person> = mutableListOf()

        for (entry in persons.entries) {
            if (entry.key != uuid)
                res.add(entry.value)
        }

        return res
    }
}