package janaja.organizer.data.model

import com.noodle.Id

data class Category(
    @Id
    var id: Long,
    var name: String
)