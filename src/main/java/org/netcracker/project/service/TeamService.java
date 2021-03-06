package org.netcracker.project.service;

import org.netcracker.project.filter.TeamFilter;
import org.netcracker.project.model.Competition;
import org.netcracker.project.model.RegisteredTeam;
import org.netcracker.project.model.Team;
import org.netcracker.project.model.User;
import org.netcracker.project.model.enums.Result;
import org.netcracker.project.model.enums.TeamRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface TeamService {
    /**
     * Метод, который возвращает страницу со всеми командами
     * @param pageable Объект Pageable, полученный из контроллера
     * @return Объект Page, содержащий страницу с командами с указанными настройками
     */
    Page<Team> getPage(Pageable pageable);

    /**
     * Метод, который возвращает страницу со всеми удовлетворяющими фильтрам командами
     * @param pageable Объект Pageable, полученный из контроллера
     * @param filter Объект TeamFilter, содержащий фильтры для команд
     * @param user пользователь, совершивший запрос и нужный для фильтров
     * @return Объект Page, содержащий страницу с командами с указанными настройками страницы и удовлетворяющий всем фильтрам
     */
    Page<Team> getPage(Pageable pageable, TeamFilter filter, User user);

    /**
     * Метод, который сохраняет команду
     * @param team сохраняемая команда
     * @param logo Файл с логотипом команды
     * @param user Пользователь, который создал команду
     * @param users Приглашенные пользователи
     * @return Булево значение, true - если команда удачно сохранена
     * @throws IOException - Исключение, которое может быть выброшено в случае ошибки сохранения логотипа
     */
    boolean save(Team team, MultipartFile logo, User user, Set<User> users) throws IOException;

    /**
     * Метод, который обновляет существующую команду
     * @param team обновляемая команда
     * @return Булево значение, true - если команда удачно обновлена
     */
    boolean update(Team team);

    /**
     * Метод, который сохраняет логотип команды
     * @param team Команда, чей логотип мы сохраняем
     * @param file Файл с изображением логотипа
     * @throws IOException - Исключение, которое может быть выброшено в случае ошибки сохранения логотипа
     */
    void saveLogo(Team team, MultipartFile file) throws IOException;

    /**
     * Метод, который обрезает логотип и сохраняет его
     * @param team Команда, чей логотип мы сохраняем
     * @param file Файл с изображением логотипа
     * @param x X координата начала обрезки
     * @param y Y координата начала обрезки
     * @param width Ширина обрезанного изображения
     * @param height Высота обрезанного изображения
     * @throws IOException - Исключение, которое может быть выброшено в случае ошибки сохранения логотипа
     */
    void cropAndSaveLogo(Team team, MultipartFile file, Integer x, Integer y, Integer width, Integer height) throws IOException;

    /**
     * Метод, который создает Групповой чат (GroupRoom) команды
     * @param team Команда, для которой создается групповой чат
     */
    void createGroupChat(Team team);

    /**
     * Метод, который добавляет в команду нового пользователя
     * @param team Команда, в которую вступает пользователь
     * @param user Вступающий пользователь
     */
    void joinTeam(Team team, User user);

    /**
     * Метод, который убирает из команды пользователя, который в ней уже состоит
     * @param team Команда, из которой убирается пользователь
     * @param user Убираемый пользователь
     */
    void quitTeam(Team team, User user);

    /**
     * Метод, который используется для награждения определенной команды
     * Метод создает новый объект статистики содержащий результат участия и само соревнование
     * @param registeredTeam Объект RegisteredTeam - аналог записанной на соревнование команды
     * @param result Результат участия, который будет присужден команде-оригиналу Team
     * @param competition Соревнование, результат участия в котором присуждается команде-оригиналу Team
     */
    void gradeTeam(RegisteredTeam registeredTeam, Result result, Competition competition);

    /**
     * Метод, который используется для сохранения нового списка командных ролей для пользователя для этой конкретной команды
     * @param team Команда, для которой пользователь изменяет список командных ролей
     * @param user Пользователь, который изменяет множество командных ролей, с которыми он представляет данную команду
     * @param teamRoles Множество командных ролей TeamRole
     */
    void saveTeamRolesByUser(Team team, User user, Set<TeamRole> teamRoles);
}
