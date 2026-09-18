local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local requested = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

local bucket = redis.call("HMGET", key ,"tokens", "timestamp")
local tokens = tonumber(bucket[1])
local last_refill = tonumber(bucket[2])

if tokens == nil then
	tokens = capacity
	last_refill = now

end

local elapsed_seconds = math.max(0,( now - last_refill) /1000)
local refilled = tokens + (elapsed_seconds * refill_rate)
tokens = math.min(capacity, refilled)


local allowed=0
local retry_after_millis=0

if tokens >= requested then
	tokens = tokens - requested
	allowed = 1

	else
	local deficit = requested - tokens
	retry_after_millis = math.ceil((deficit/refill_rate)*1000)

	end

redis.call("HMSET", key, "tokens",tostring(tokens), "timestamp", tostring(now))
redis.call ("EXPIRE", key, 3600)

return { allowed, tostring(tokens),retry_after_millis}
