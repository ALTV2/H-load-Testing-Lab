#!/bin/bash

# Директория для сохранения отчетов wrk2
REPORT_DIR="./wrk_reports"
mkdir -p $REPORT_DIR

# Начальное количество соединений и длительность теста
CONNECTIONS=1
DURATION="30s"
THREADS=1


# Базовая команда для запуска wrk2
WRK_CMD="/./Users/tveritinaleksandr/Tools/wrk2-arm/wrk -s students_generator.lua -c $CONNECTIONS -d $DURATION -t $THREADS -R 100 -L http://localhost:8080"

# Функция для проверки ответа сервера
check_server_response() {
    local response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/students)
    if [[ $response != "200" ]]; then
        return 1  # Сервер не отвечает с кодом 200 OK
    else
        return 0  # Сервер отвечает корректно
    fi
}

# Увеличиваем нагрузку до тех пор, пока сервер не откажет или не достигнут лимит соединений
while true; do
    echo "Запуск с $CONNECTIONS соединениями..."
    # Запуск wrk2 и сохранение отчета
    $WRK_CMD > "$REPORT_DIR/wrk_report_$CONNECTIONS.txt"
    # Проверка ответа сервера
    if ! check_server_response; then
        echo "Сервер вышел из строя при $CONNECTIONS соединениях."
        break
    fi

    # Увеличение количества соединений для следующей итерации
    CONNECTIONS=$((CONNECTIONS * 2))
    if [ $CONNECTIONS -gt 1000 ]; then  # Произвольный максимальный лимит, можно настроить
        echo "Достигнут максимальный лимит соединений."
        break
    fi
done

echo "Тестирование завершено. Отчеты сохранены в $REPORT_DIR."