#!/bin/bash

# Определение констант и переменных
CONNECTIONS=12
DURATION="10s"
THREADS=4

# Базовая команда для запуска wrk2
WRK_CMD="/./Users/tveritinaleksandr/Tools/wrk2-arm/wrk -s students_generator.lua -c $CONNECTIONS -d $DURATION -t $THREADS -R 500 -L http://localhost:8080"
readonly REPORT_FILE="report.txt"
readonly THRESHOLD_ERROR_RATE=0.01  # Порог ошибок - 1%

# Запуск нагрузочного теста и сохранение вывода
$WRK_CMD > $REPORT_FILE

# Анализ результатов нагрузочного теста
error_count=$(grep -o "Non-2xx or 3xx responses: [0-9]*" $REPORT_FILE | sed 's/[^0-9]*//g')
total_requests=$(grep "Requests/sec" $REPORT_FILE | awk '{print $2}' | sed 's/[^0-9]*//g')

if [ -z "$error_count" ]; then
    error_count=0
fi

if [ -z "$total_requests" ] || [ "$total_requests" -eq 0 ]; then
    echo "Ошибка: не удалось определить количество запросов."
    exit 1
fi

error_rate=$(echo "scale=4; $error_count / $total_requests" | bc)

# Проверка процента ошибок
if (( $(echo "$error_rate > $THRESHOLD_ERROR_RATE" | bc -l) )); then
    echo "При проведении нагрузочного тестирования ошибочный процент запросов (кол-во ошибок $error_count) превышает допустимый порог ($THRESHOLD_ERROR_RATE)."
else
    echo "Все запросы к серверу З/П работников успешно завершились. Ошибочный процент: $error_rate"
fi

## Подсчет количества ответов, которые пришли дольше чем за 20 секунд
#slow_responses=$(grep -A 10 "Latency Distribution" $REPORT_FILE | grep '^100.000%' | awk '{print $2}' | awk -F 's' '{if ($1 > 20) print $1}' | wc -l)
#
#echo "Количество ответов, время которых превысило 20 секунд: $slow_responses"

# Проверка статуса контейнеров
containers_to_check=(
    "HT-mongo"
    "Dean-postgres"
    "Dean-kafka"
    "Dean-zookeeper"
    "Dean-kafka-consumer"
    "Dean-spark"
    "Dean-office-service"
)

# Получение списка запущенных контейнеров
running_containers=$(docker ps --format "{{.Names}}")

# Определение остановленных контейнеров
stopped_containers=()
for container in "${containers_to_check[@]}"; do
    if ! echo "$running_containers" | grep -q "$container"; then
        stopped_containers+=("$container")
    fi
done

# Отчет о статусе контейнеров
if [ ${#stopped_containers[@]} -gt 0 ]; then
    echo "Список упавших контейнеров:"
    printf '* %s\n' "${stopped_containers[@]}"
else
    echo "Все контейнеры остались в рабочем состоянии"
fi

# Проверка свободного места на диске
disk_space=$(df -h / | awk '/\// {print $5}' | sed 's/%//')
if [ "$disk_space" -gt 90 ]; then
    echo "Внимание: использование дискового пространства превышает 90%!"
fi