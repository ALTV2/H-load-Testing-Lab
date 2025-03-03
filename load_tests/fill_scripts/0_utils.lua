function randomString(length)
    local res = ""
    for i = 1, length do
        res = res .. string.char(math.random(97, 122))
    end
    return res
end

function randomEmail()
    return randomString(5) .. "@example.com"
end

function randomPhone()
    return "+1" .. string.format("%09d", math.random(0, 999999999))
end

function randomDate()
    local year = math.random(1995, 2005)
    local month = string.format("%02d", math.random(1, 12))
    local day = string.format("%02d", math.random(1, 28))
    return year .. "-" .. month .. "-" .. day
end

function randomTime()
    local hour = string.format("%02d", math.random(8, 18))
    local minute = string.format("%02d", math.random(0, 59))
    return hour .. ":" .. minute .. ":00"
end

daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}

math.randomseed(os.time())