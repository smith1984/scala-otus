package module4.homework.services

import zio.Has
import zio.Task
import module4.homework.dao.entity.User
import module4.homework.dao.entity.Role
import module4.homework.dao.repository.UserRepository
import zio.ZIO
import zio.RIO
import module4.homework.dao.entity.UserToRole
import zio.ZLayer
import zio.macros.accessible
import module4.homework.dao.entity.RoleCode
import module4.phoneBook.db

@accessible
object UserService {
  type UserService = Has[Service]

  trait Service {
    def listUsers(): RIO[db.DataSource, List[User]]
    def listUsersDTO(): RIO[db.DataSource, List[UserDTO]]
    def addUserWithRole(
                         user: User,
                         roleCode: RoleCode
                       ): RIO[db.DataSource, UserDTO]
    def listUsersWithRole(roleCode: RoleCode): RIO[db.DataSource, List[UserDTO]]
  }

  class Impl(userRepo: UserRepository.Service) extends Service {
    val dc = db.Ctx
    import dc._

    def listUsers(): RIO[db.DataSource, List[User]] =
      userRepo.list()

    def usersWithRoles(users: List[User]): RIO[db.DataSource, List[UserDTO]] = {
      ZIO.foreach(users) { user =>
      {
        for {
          roles <- userRepo.userRoles(user.typedId)
        } yield UserDTO(user, roles.toSet)
      }
      }
    }
    def listUsersDTO(): RIO[db.DataSource, List[UserDTO]] =
      listUsers().flatMap(usersWithRoles)

    def addUserWithRole(
                         user: User,
                         roleCode: RoleCode
                       ): RIO[db.DataSource, UserDTO] = transaction(
      for {
        user <- userRepo.createUser(user)
        _ <- userRepo.insertRoleToUser(roleCode, user.typedId)
        roles <- userRepo.userRoles(user.typedId)
      } yield UserDTO(user, roles.toSet)
    )

    def listUsersWithRole(
                           roleCode: RoleCode
                         ): RIO[db.DataSource, List[UserDTO]] =
      userRepo.listUsersWithRole(roleCode).flatMap(usersWithRoles)
  }

  val live: ZLayer[UserRepository.UserRepository, Nothing, UserService] =
    ZLayer.fromService(repo => new Impl(repo))
}

case class UserDTO(user: User, roles: Set[Role])
