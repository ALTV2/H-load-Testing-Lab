#!/bin/bash

# Параметры теста
CONNECTIONS=1
DURATION="10s"  # Увеличено для завершения всех запросов
THREADS=1
RATE=2
WRK_PATH="/./Users/tveritinaleksandr/Tools/wrk2-arm/wrk"  # Убрано лишнее /.
BASE_URL="http://localhost:8080"
REPORT_DIR="reports"

# Создание директории для отчетов
mkdir -p $REPORT_DIR

# Функция для запуска теста и анализа отчета
run_test() {
    local GENERATE_SCRIPT=$1
    local report_file="$REPORT_DIR/${GENERATE_SCRIPT}_report.txt"
    local cmd="$WRK_PATH -s $GENERATE_SCRIPT -c $CONNECTIONS -d $DURATION -t $THREADS -R $RATE -L $BASE_URL"

    echo "Запуск теста для $GENERATE_SCRIPT..."
    echo "Команда: $cmd"
    $cmd > $report_file 2>&1

    # Анализ отчета
    total_requests=$(grep "Requests/sec" $report_file | awk '{print $2}' | sed 's/[^0-9.]*//g')
    error_count=$(grep -o "Non-2xx or 3xx responses: [0-9]*" $report_file | sed 's/[^0-9]*//g' || echo "0")
    latency_avg=$(grep "Latency" $report_file | head -1 | awk '{print $2}' | sed 's/[^0-9.]//g' || echo "N/A")
    latency_max=$(grep "Latency" $report_file | head -1 | awk '{print $4}' | sed 's/[^0-9.]//g' || echo "N/A")
    latency_90=$(grep "90.000%" $report_file | awk '{print $2}' | sed 's/[^0-9.]//g' || echo "N/A")

    if [ -z "$total_requests" ] || [ "$total_requests" = "0" ]; then
        echo "Ошибка: не удалось определить количество запросов для $GENERATE_SCRIPT."
        cat $report_file
        return 1
    fi

    error_rate=$(echo "scale=4; ($error_count / $total_requests) * 100" | bc)
    if [ -z "$error_rate" ]; then
        error_rate="N/A"
    fi
    echo "----------------------------------------"
    echo "Отчет для $GENERATE_SCRIPT:"
    echo "Количество запросов в секунду: $total_requests"
    echo "Количество ошибок: $error_count"
    echo "Процент ошибок: $error_rate%"
    echo "Средняя задержка: $latency_avg мс"
    echo "Максимальная задержка: $latency_max мс"
    echo "90-й процентиль задержки: $latency_90 мс"
    echo "----------------------------------------"
}

# Последовательный запуск тестов
run_test "1_teacher_generator.lua"
sleep 5
run_test "2_group_generator.lua"
sleep 5
run_test "3_subject_generator.lua"
sleep 5
run_test "4_student_generator.lua"
sleep 5
run_test "5_grade_generator.lua"
sleep 5
run_test "6_schedule_generator.lua"

# Проверка статуса контейнеров
containers_to_check=("HT-mongo" "Dean-postgres" "Dean-kafka" "Dean-zookeeper" "Dean-office-service")
running_containers=$(docker ps --format "{{.Names}}")
stopped_containers=()

for container in "${containers_to_check[@]}"; do
    if ! echo "$running_containers" | grep -q "$container"; then
        stopped_containers+=("$container")
    fi
done

if [ ${#stopped_containers[@]} -gt 0 ]; then
    echo "Упавшие контейнеры:"
    printf '* %s\n' "${stopped_containers[@]}"
else
    echo "Все контейнеры работают"
fi

# Проверка дискового пространства
disk_space=$(df -h / | awk '/\// {print $5}' | sed 's/%//')
if [ "$disk_space" -gt 90 ]; then
    echo "Внимание: использование диска превышает 90% ($disk_space%)"
fi