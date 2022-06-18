package tech.ixor.entity

class QQGroupEntity constructor(
    val groupName: String, val groupCode: Long
)

object QQGroups {
    private val qqGroups = mutableListOf<QQGroupEntity>()

    fun addQQGroup(qqGroup: QQGroupEntity) {
        qqGroups.add(qqGroup)
    }

    fun addQQGroup(qqGroup: ConfigEntity.QQGroupConfig) {
        qqGroups.add(QQGroupEntity(qqGroup.groupName, qqGroup.groupCode))
    }

    fun addQQGroup(groupName: String, groupCode: Long) {
        qqGroups.add(QQGroupEntity(groupName, groupCode))
    }

    fun getQQGroups(): List<QQGroupEntity> {
        return qqGroups
    }

    fun getQQGroup(groupCode: Long): QQGroupEntity? {
        return qqGroups.find { it.groupCode == groupCode }
    }
}
