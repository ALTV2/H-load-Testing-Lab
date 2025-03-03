require "0_utils"

scheduleCount = 0
TARGET_SCHEDULES = 5

dofile("groups.lua")
dofile("subjects.lua")
dofile("teachers.lua")
if not groups then groups = {} end
if not subjects then subjects = {} end
if not teachers then teachers = {} end

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"
wrk.headers["Host"] = "localhost:8080"

function response(status, headers, body)
    if status == 200 or status == 201 then
        scheduleCount = scheduleCount + 1
    else
        print("Ошибка сервера: статус " .. status .. ", тело: " .. body)
    end
end

function request()
    if scheduleCount < TARGET_SCHEDULES then
        if #groups == 0 or #subjects == 0 or #teachers == 0 then
            print("Ошибка: нет доступных групп, предметов или учителей")
            return nil
        end
        local path = "/api/schedules"
        local groupId = groups[math.random(1, #groups)]
        local subjectId = subjects[math.random(1, #subjects)]
        local teacherId = teachers[math.random(1, #teachers)]
        local body = string.format([[
        {
          "group": {"id": "%s"},
          "subject": {"id": "%s"},
          "teacher": {"id": "%s"},
          "dayOfWeek": "%s",
          "startTime": "%s",
          "endTime": "%s"
        }
        ]], groupId, subjectId, teacherId, daysOfWeek[math.random(1, #daysOfWeek)], randomTime(), randomTime())
        return wrk.format("POST", path, nil, body)
    end
    return nil
end