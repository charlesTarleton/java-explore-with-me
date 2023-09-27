package ru.practicum.exploreWithMe.commonFiles.utils;

public final class ConstantaClass {
    public static final class Admin {
        private static final String END = "администратора получил запрос на {}{}";
        public static final String USER_CONTROLLER_LOG = "Контроллер пользователей " + END;
        public static final String COMPILATION_CONTROLLER_LOG = "Контроллер подборок событий " + END;
        public static final String EVENT_CONTROLLER_LOG = "Контроллер событий " + END;
        public static final String CATEGORY_CONTROLLER_LOG = "Контроллер категорий " + END;
        public static final String USER_SERVICE_LOG = "Сервис пользователей " + END;
        public static final String COMPILATION_SERVICE_LOG = "Сервис подборок событий " + END;
        public static final String EVENT_SERVICE_LOG = "Сервис событий " + END;
        public static final String CATEGORY_SERVICE_LOG = "Сервис категорий " + END;
        public static final byte REQUIREMENT_HOURS_COUNT = 1;
    }

    public static final class Private {
        private static final String START = "Частный ";
        private static final String END = "получил запрос на {}{}";
        public static final String REQUEST_CONTROLLER_LOG = START + "контроллер запросов " + END;
        public static final String EVENT_CONTROLLER_LOG = START + "контроллер событий " + END;
        public static final String REQUEST_SERVICE_LOG = START + "сервис запросов " + END;
        public static final String EVENT_SERVICE_LOG = START + "сервис событий " + END;
        public static final byte REQUIREMENT_HOURS_COUNT = 2;
    }

    public static final class Public {
        private static final String START = "Публичный ";
        private static final String END = "получил запрос на {}{}";
        public static final String COMPILATION_CONTROLLER_LOG = START + "контроллер подборок событий " + END;
        public static final String EVENT_CONTROLLER_LOG = START + "контроллер событий " + END;
        public static final String CATEGORY_CONTROLLER_LOG = START + "контроллер категорий " + END;
        public static final String COMPILATION_SERVICE_LOG = START + "сервис подборок событий " + END;
        public static final String EVENT_SERVICE_LOG = START + "сервис событий " + END;
        public static final String CATEGORY_SERVICE_LOG = START + "сервис категорий " + END;
    }

    public static final class Common {
        public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    }
}
